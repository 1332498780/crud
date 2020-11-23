package com.cn.tfe.controller;

import com.alibaba.excel.EasyExcel;
import com.cn.tfe.dto.VocabuDto;
import com.cn.tfe.entity.CommonRes;
import com.cn.tfe.entity.Vocabu;
import com.cn.tfe.exception.CustomException;
import com.cn.tfe.query.VocabuFilterParam;
import com.cn.tfe.repository.VocabuMongoRepository;
import com.cn.tfe.util.RequestPageData;
import com.cn.tfe.util.ResponseData;
import com.cn.tfe.util.ResponsePage;
import com.cn.tfe.util.baidu.TransApi;
import com.cn.tfe.util.easypoi.DemoDataListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.val;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@RestController
@RequestMapping(value="/vocabu")
public class VocabuController {

    private static final Logger log = LoggerFactory.getLogger(VocabuController.class);

    @Autowired
    VocabuMongoRepository vocabuMongoRepository;

    @Value("${baidu.appid}")
    private String appid;

    @Value("${baidu.securityKey}")
    private String securityKey;


    @PostMapping("/page")
    public ResponseData<List<Vocabu>> getPageList(@RequestBody RequestPageData<VocabuFilterParam> requestPageData){
        if(requestPageData.getData()==null){
            Page<Vocabu> pageData = vocabuMongoRepository.findAll(requestPageData.pgData());
            return ResponsePage.page(pageData);
        }
        VocabuFilterParam param = requestPageData.getData();
        String word = param.getWord();
        String dst = param.getDst();
        return  vocabuMongoRepository.findByWordAndDict(word,dst,requestPageData.getPage(),requestPageData.getSize());
    }

    @PostMapping("/add")
    public ResponseData addVocabu(@RequestBody Vocabu vocabu){
        vocabu.setWord(vocabu.getWord().toLowerCase());
        Vocabu savedVocabu = vocabuMongoRepository.insert(vocabu);
        if(savedVocabu != null && savedVocabu.getId()!=null){
            return ResponseData.SUCCESS;
        }
        throw new CustomException("新增单词失败");
    }

    @GetMapping("/del/{id}")
    public ResponseData delVocabu(@PathVariable String id){
        Boolean isExisted = vocabuMongoRepository.existsById(id);
        if(isExisted){
            vocabuMongoRepository.deleteById(id);
            return ResponseData.SUCCESS;
        }
        throw new CustomException("删除单词失败");
    }

    @GetMapping("/get/{id}")
    public ResponseData<Vocabu> getVocabu(@PathVariable String id){
        Optional<Vocabu> option = vocabuMongoRepository.findById(id);
        if(option.isPresent()){
            return ResponseData.of(option.get());
        }
        throw new CustomException("不存在的id");
    }

    @PostMapping("/update/{id}")
    public ResponseData updateVocabu(@PathVariable String id,@RequestBody Vocabu vocabu){
        Boolean isExisted = vocabuMongoRepository.existsById(id);
        if(isExisted){
            vocabu.setId(id);
            vocabu.setWord(vocabu.getWord().toLowerCase());
            Vocabu savedVocabu = vocabuMongoRepository.save(vocabu);
            if(savedVocabu!=null){
                return ResponseData.SUCCESS;
            }
        }
        throw new CustomException("更新单词失败");
    }

    @GetMapping("getRemplate")
    public void getRemplate(@RequestParam("param") String  param,
        HttpServletResponse response
    ) throws IOException {
        String[] params = param.split(",");
        if(params.length <= 0){
            log.error("异常参数:"+param);
            throw new CustomException("参数异常");
        }
        TransApi api = new TransApi(appid, securityKey);
        List<VocabuDto> vocabuDtos = new ArrayList<>();
        //转化为小写
        for(int i=0;i<params.length;i++){
            String queryStr = params[i].toLowerCase();
            JsonObject en2zhObj = sendRequest(api,queryStr,Language.EN, Language.ZH);
            JsonObject dictObj = JsonParser.parseString(en2zhObj.getAsJsonPrimitive("dict")
                    .getAsString()).getAsJsonObject();
            JsonArray trGroup = dictObj.getAsJsonObject("word_result")
                    .getAsJsonObject("edict").getAsJsonArray("item")
                    .get(0).getAsJsonObject().getAsJsonArray("tr_group");
            String eDict = trGroup.get(0).getAsJsonObject().getAsJsonArray("tr").get(0).getAsString();
            CommonRes transRes = getCommonRes(api,queryStr);
            List<CommonRes> exampleRes = getWordSentence(api,trGroup,queryStr,"example");
            List<CommonRes> similarRes = getWordSentence(api,trGroup,queryStr,"similar_word");

            VocabuDto vocabuDto = VocabuDto.builder()
                    .word(queryStr)
                    .edict(eDict)
                    .transRes(transRes)
                    .similarRes(similarRes)
                    .exampleRes(exampleRes)
                    .build();
            vocabuDtos.add(vocabuDto);
        }
        response.setHeader("Content-Type", "application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=导入模板");
        EasyExcel.write(response.getOutputStream(),VocabuDto.class).sheet("模板").doWrite(vocabuDtos);
    }

    /***
     * 请求翻译接口，得到JsonObject对象
     * @param api
     * @param query
     * @param from
     * @param to
     * @return
     */
    private JsonObject sendRequest(TransApi api,String query, Language from, Language to){
        String en2zhRes = api.getTransResult(query, from.toString(), to.toString());
        JsonElement element = JsonParser.parseString(en2zhRes);
        if(!element.isJsonObject()){
            log.error("api返回数据："+en2zhRes);
            throw new CustomException("请求api返回数据格式非法");
        }
        JsonObject res = element.getAsJsonObject();
        log.info(res.toString());
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
        JsonObject zh2enObj = sendRequest(api,dst,Language.ZH,Language.EN);
//        JsonElement dictEle = JsonParser.parseString(zh2enObj.getAsJsonPrimitive("dict").getAsString());
//        String phonetic = "";
//        if(dictEle.isJsonNull()){
//            phonetic = getPhonetic(dst);
//        }else if(dictEle.isJsonObject()){
//            JsonObject dictObj = dictEle.getAsJsonObject();
//            phonetic = dictObj.getAsJsonObject("word_result")
//                    .getAsJsonObject("zdict")
//                    .getAsJsonObject("simple")
//                    .getAsJsonArray("means")
//                    .get(0).getAsJsonObject().get("pinyin").getAsString();
//        }
        String phonetic = getPhonetic(dst);
        String dstTts = zh2enObj.get("dst_tts").getAsString();
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
                if(res.length >= 0){
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
    @PostMapping("/upload")
    public ResponseData uploadFile(@RequestPart("file") MultipartFile multipartFile) throws IOException {
        String fileName = multipartFile.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf('.')+1);
        if(!suffix.equals("xls") && !suffix.equals("xlsx")) {
            throw new CustomException("文件格式有误，请使用xls/xlsx格式文件");
        }
        EasyExcel.read(multipartFile.getInputStream(),VocabuDto.class,new DemoDataListener<Vocabu,VocabuDto,String>(vocabuMongoRepository)).sheet().doRead();
        return ResponseData.SUCCESS;
    }

    public enum Language{
        EN("en"),
        ZH("zh");

        public String val;
        private Language(String val){
            this.val = val;
        }
        public String toString(){
            return this.val;
        }
    }
}
