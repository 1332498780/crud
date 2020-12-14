package com.cn.tfe.service;

import com.cn.tfe.controller.VocabuController;
import com.cn.tfe.dto.VocabuDto;
import com.cn.tfe.emums.Language;
import com.cn.tfe.entity.CommonRes;
import com.cn.tfe.entity.Vocabu;
import com.cn.tfe.entity.VocabuTrans;
import com.cn.tfe.exception.CustomException;
import com.cn.tfe.util.baidu.TransApi;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Future;
import java.util.regex.Pattern;


@Service
public class AsynVocabuService {

    private static final Logger log = LoggerFactory.getLogger(AsynVocabuService.class);

    @Async("taskExecutor")
    public Future<Vocabu> executeGetVocabuDto(TransApi api, String word, Language from , Language to){
        log.info(Thread.currentThread().getName()+" get ready to execute "+"< "+word+" >");
        JsonObject en2zhObj = sendRequest(api,word, from, to);

        Vocabu.VocabuBuilder builder = Vocabu.builder().word(word);
        CommonRes transRes = getCommonRes(api,word);
        builder.transRes(transRes);
        JsonElement dictElement = JsonParser.parseString(en2zhObj.getAsJsonPrimitive("dict").getAsString());
        if(dictElement.isJsonObject()){
            JsonElement edictElement = dictElement.getAsJsonObject().getAsJsonObject("word_result").get("edict");
            if(edictElement.isJsonPrimitive()){
                log.warn("word: ["+word+"] 'edict is json primitive!!!");
            }else if(edictElement.isJsonObject()) {
                JsonArray trGroup = edictElement.getAsJsonObject().getAsJsonArray("item")
                        .get(0).getAsJsonObject().getAsJsonArray("tr_group");
                String eDict = trGroup.get(0).getAsJsonObject().getAsJsonArray("tr").get(0).getAsString();
                List<CommonRes> exampleRes = getWordSentence(api, trGroup, word, "example");
                List<CommonRes> similarRes = getWordSentence(api, trGroup, word, "similar_word");
                builder.word(word)
                        .edict(eDict)
                        .similarRes(similarRes)
                        .exampleRes(exampleRes);
            }
        }
//        if(dictObj.get("word_result").isJsonPrimitive()){
//            log.info("word: ["+word+"] 'word_result is json primitive!!!");
//        }else if(dictObj.get("word_result").isJsonObject()){
//            JsonObject wordResultObj = dictObj.getAsJsonObject("word_result");
//        }
        return new AsyncResult<>(builder.build());
    }

    private String transferToOther(TransApi api, String word, Language to){
        JsonObject en2OtherObj = sendRequest(api,word, Language.EN, to);
        return en2OtherObj.getAsJsonPrimitive("dst").getAsString();
    }

    @Async("taskExecutor")
    public Future<List<VocabuTrans>> executeGetVocabuTrans(TransApi api, Vocabu vocabu, Language[] transLanguages){
        String word = vocabu.getWord();
        log.info(Thread.currentThread().getName()+" get ready to transfer "+"< "+word+" > to multi language.");
        List<VocabuTrans> vocabuTrans = new ArrayList<>(transLanguages.length);
        for(Language lan:transLanguages){
            VocabuTrans.VocabuTransBuilder builder = VocabuTrans.builder().word(word);

            //单词翻译
            String dst = transferToOther(api,word,lan);
            builder.transRes(CommonRes.builder().dst(dst).build());
            //例句翻译
            List<CommonRes> examples = new ArrayList<>(vocabu.getExampleRes().size());
            for(CommonRes comm:vocabu.getExampleRes()){
                String exampleDst = transferToOther(api,comm.getWord(),lan);
                CommonRes commonRes = CommonRes.builder().word(comm.getWord()).dst(exampleDst).build();
                examples.add(commonRes);
            }
            if(!examples.isEmpty()){
                builder.exampleRes(examples);
            }
            //句子翻译
            List<CommonRes> similars = new ArrayList<>(vocabu.getSimilarRes().size());
            for(CommonRes comm:vocabu.getSimilarRes()){
                String similarDst = transferToOther(api,comm.getWord(),lan);
                CommonRes commonRes = CommonRes.builder().word(comm.getWord()).dst(similarDst).build();
                similars.add(commonRes);
            }
            if(!similars.isEmpty()){
                builder.similarRes(similars);
            }

            builder.fromTo(Language.EN.num*10+lan.num);
            vocabuTrans.add(builder.build());
        }
        log.info(Thread.currentThread().getName()+" has finished "+"< "+word+" > to multi language.");
        return new AsyncResult<>(vocabuTrans);
    }

    /***
     * 请求翻译接口，得到JsonObject对象
     * @param api
     * @param query
     * @param from
     * @param to
     * @return
     */
    private JsonObject sendRequest(TransApi api, String query, Language from, Language to){
        String en2zhRes = api.getTransResult(query, from.toString(), to.toString());
        JsonElement element = JsonParser.parseString(en2zhRes);
        if(!element.isJsonObject()){
            log.error("api返回数据："+en2zhRes);
            throw new CustomException("请求api返回数据格式非法");
        }
        JsonObject res = element.getAsJsonObject();
//        log.info(res.toString());
        return res.getAsJsonArray("trans_result").get(0).getAsJsonObject();
    }

    private List<CommonRes> getWordSentence(TransApi api,JsonArray trGroup,String word,String key){
        List<CommonRes> res = new ArrayList<>();
        Iterator<JsonElement> iterator = trGroup.iterator();
        String patternStr = "\\b"+word+"\\b";
        Pattern pattern = Pattern.compile(patternStr);
        while(iterator.hasNext()){
            JsonObject item = iterator.next().getAsJsonObject();
            JsonElement element = item.get(key);
            if(!element.isJsonNull() && element.isJsonArray()){
                JsonArray jsonArray = element.getAsJsonArray();
                Iterator<JsonElement> childIterator = jsonArray.iterator();
                while(childIterator.hasNext()){
                    String str = childIterator.next().getAsString();
                    if(pattern.matcher(str.toLowerCase()).find()){
                        CommonRes commonRes = getCommonRes(api,str);
                        res.add(commonRes);
                    }
                }
            }
        }
        return res;
    }


    /***
     * 请求指定英文单词 并获得其汉语，汉语拼音，汉语翻译连接，装入CommonRes中
     * @param api
     * @param word 指定单词
     * @return
     */
    private CommonRes getCommonRes(TransApi api,String word){
        //请求接口获取释义
        JsonObject en2zhObj = sendRequest(api,word,Language.EN,Language.ZH);
        return getCommonRes(api,en2zhObj,word);
    }

    private CommonRes getCommonRes(TransApi api,JsonObject en2zhObj,String word){
        String dst = en2zhObj.get("dst").getAsString();
//        JsonObject zh2enObj = sendRequest(api,dst,Language.ZH,Language.EN);
        String phonetic = getPhonetic(dst);
        String dstTts = en2zhObj.get("dst_tts").getAsString();
        CommonRes commonRes = CommonRes.builder()
                .word(word)
                .phonetic(phonetic)
                .dst(dst)
                .dstTts(dstTts)
                .build();
        return commonRes;
    }

    private String getPhonetic(String chinese){
        HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();
        outputFormat.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);
        outputFormat.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
        outputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        StringBuilder builder = new StringBuilder();
        for(char c : chinese.toCharArray()){
            try {
                String[] res = PinyinHelper.toHanyuPinyinStringArray(c,outputFormat);
                if(res != null && res.length >= 0){
                    builder.append(res[0]).append(' ');
                }
            } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                badHanyuPinyinOutputFormatCombination.printStackTrace();
                log.error(badHanyuPinyinOutputFormatCombination.getMessage());
            }
        }
        if(builder.length() > 1){
            return builder.deleteCharAt(builder.length()-1).toString();
        }
        return builder.toString();
    }


}
