package com.cn.tfe.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.cn.tfe.dto.VocabuDto;
import com.cn.tfe.emums.Language;
import com.cn.tfe.entity.Vocabu;
import com.cn.tfe.exception.CustomException;
import com.cn.tfe.query.VocabuFilterParam;
import com.cn.tfe.repository.VocabuMongoRepository;
import com.cn.tfe.service.AsynVocabuService;
import com.cn.tfe.util.RequestPageData;
import com.cn.tfe.util.ResponseData;
import com.cn.tfe.util.ResponsePage;
import com.cn.tfe.util.baidu.TransApi;
import com.cn.tfe.util.easypoi.DemoDataListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

@RestController
@RequestMapping(value="/vocabu")
public class VocabuController {

    private static final Logger log = LoggerFactory.getLogger(VocabuController.class);

    @Autowired
    AsynVocabuService asynVocabuService;

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

    @GetMapping("/getAddition/{id}")
    public ResponseData<Vocabu> getVocabuAddition(@PathVariable String id){
        Optional<Vocabu> option = vocabuMongoRepository.findById(id);
        if(option.isPresent()){
            Vocabu vocabu = option.get();
            String word = vocabu.getWord();
            List<Vocabu> res = vocabuMongoRepository.findByExampleWord(word);
            for(Vocabu v:res){
                vocabu.getSimilarRes().addAll(v.getSimilarRes());
            }
            return ResponseData.of(vocabu);
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

//    @PostMapping("getTemplate")
//    public void getRemplate(@RequestBody Map<String,List<String>>  requestParam,
//        HttpServletResponse response
//    ) throws IOException {
//        List<String> params = requestParam.get("param");
//        if(params == null && params.size() <= 0){
//            log.error("异常参数:"+params);
//            throw new CustomException("参数异常");
//        }
//        TransApi api = new TransApi(appid, securityKey);
//        List<VocabuDto> vocabuDtos = new ArrayList<>();
//        //转化为小写
//        for(int i=0;i<params.size();i++){
//            String queryStr = params.get(i).toLowerCase();
//            JsonObject en2zhObj = sendRequest(api,queryStr,Language.EN, Language.ZH);
//            JsonObject dictObj = JsonParser.parseString(en2zhObj.getAsJsonPrimitive("dict")
//                    .getAsString()).getAsJsonObject();
//            JsonArray trGroup = dictObj.getAsJsonObject("word_result")
//                    .getAsJsonObject("edict").getAsJsonArray("item")
//                    .get(0).getAsJsonObject().getAsJsonArray("tr_group");
//            String eDict = trGroup.get(0).getAsJsonObject().getAsJsonArray("tr").get(0).getAsString();
//            CommonRes transRes = getCommonRes(api,queryStr);
//            List<CommonRes> exampleRes = getWordSentence(api,trGroup,queryStr,"example");
//            List<CommonRes> similarRes = getWordSentence(api,trGroup,queryStr,"similar_word");
//
//            VocabuDto vocabuDto = VocabuDto.builder()
//                    .word(queryStr)
//                    .edict(eDict)
//                    .transRes(transRes)
//                    .similarRes(similarRes)
//                    .exampleRes(exampleRes)
//                    .build();
//            vocabuDtos.add(vocabuDto);
//        }
//        response.setHeader("Content-Type", "application/vnd.ms-excel");
//        response.setHeader("Content-Disposition", "attachment; filename=导入模板");
//
//
//        try {
//            response.setContentType("application/vnd.ms-excel");
//            response.setCharacterEncoding("utf-8");
//            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
//            String fileName = URLEncoder.encode("导入模板", "UTF-8").replaceAll("\\+", "%20");
//            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
//            // 这里需要设置不关闭流
//            EasyExcel.write(response.getOutputStream(),VocabuDto.class).autoCloseStream(Boolean.FALSE)
//                    .sheet("模板").doWrite(vocabuDtos);
//        } catch (Exception e) {
//            // 重置response
//            response.reset();
//            response.setContentType("application/json");
//            response.setCharacterEncoding("utf-8");
//            response.getWriter().println("{\"code\":500,data:\""+e.getMessage()+"\"}");
//        }
//    }

    @PostMapping("writeBatch")
    public void getRemplate(@RequestBody Map<String,List<String>>  requestParam,
                            HttpServletResponse response
    ) throws IOException {
        long startTime = System.currentTimeMillis();
        log.info("writeBatch start >>> "+startTime);

        List<String> params = requestParam.get("param");
        if(params == null && params.size() <= 0){
            log.error("异常参数:"+params);
            throw new CustomException("参数异常");
        }
        TransApi api = new TransApi(appid, securityKey);
        List<Vocabu> vocabus = new ArrayList<>();
        List<Future<Vocabu>> futures = new ArrayList<>();
        List<Future<Vocabu>> doneFutures = new ArrayList<>();

        Iterable<String> iterable = cleaningWordData(params);
        iterable.forEach(word ->{
            Future<Vocabu> vocabuDtoFuture = asynVocabuService.executeGetVocabuDto(api,word, Language.EN,Language.ZH);
            futures.add(vocabuDtoFuture);
        });

        //写文件参数
//        String fileName = "D:\\项目\\private\\导入导出\\write.xlsx";
//        ExcelWriter excelWriter = EasyExcel.write(fileName, VocabuDto.class).build();
//        WriteSheet writeSheet = EasyExcel.writerSheet("模板").build();

        int count = 0;
        try {
            while (true) {
                count++;
                for (Future<Vocabu> f : futures) {
                    if (!doneFutures.contains(f) && f.isDone() && !f.isCancelled()) {
                        doneFutures.add(f);
                        try {
                            vocabus.add(f.get());
                            if (vocabus.size() >= 200) {
                                log.info("executor has 200 finished rows and get ready to write into xlsx.");
//                                excelWriter.write(vocabuDtos, writeSheet);
                                vocabuMongoRepository.insert(vocabus);
                                vocabus.clear();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if(count % 100 == 0)
                    log.info("executor has "+count+" looped!  < "+doneFutures.size()+"/"+futures.size()+" >");
                if(doneFutures.size() == futures.size()){
                    log.info("finished!");
                    break;
                }
                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            //把剩余数据写完
            if(vocabus.size() > 0){
                log.info("executor deal with remain data("+vocabus.size()+") of list");
                vocabuMongoRepository.insert(vocabus);
//                excelWriter.write(vocabuDtos, writeSheet);
            }
            long endTime = System.currentTimeMillis();
            log.info("writeBatch end >>> "+endTime);
            log.info("totally cost "+((endTime - startTime)/1000) +" s ");
//            if (excelWriter != null) {
//                excelWriter.finish();
//            }
        }
    }

    /***
     * 清洗数据，所有单词均改为小写，并去重。目前不考虑 "a.m" 这种缩写格式
     * @param source
     * @return
     */
    private Iterable<String> cleaningWordData(List<String> source){
        Set<String> res = new HashSet<>();
        StringBuilder sb = new StringBuilder("[");
        int count = 0;
        //英文最长单词45个字母 pneumonoultramicroscopicsilicovolcanoconiosis [肺尘病]
        String regexStr = "^[a-z]{1,45}$";
        for(String word:source){
            String lowwer = word.trim().toLowerCase();
            if(lowwer.matches(regexStr)){
                res.add(lowwer);
            }else{
                count++;
                sb.append(word).append(",");
            }
        }
        if(sb.toString().endsWith(",")){
            // 注意不能写成 sb.replace(len-1,len-1,"]") 头尾相等的话，会多添加一个"]"在","之前
            sb.replace(sb.length()-1,sb.length(),"]");
        }
        log.info("cleaning data ("+count+"/"+source.size()+"): "+sb.toString());
        return res;
    }

    @PostMapping("/upload")
    public ResponseData uploadFile(@RequestPart("file") MultipartFile multipartFile) throws IOException {
        String fileName = multipartFile.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf('.')+1);
        if(!suffix.equals("xls") && !suffix.equals("xlsx")) {
            throw new CustomException("文件格式有误，请使用xls/xlsx格式文件");
        }
        EasyExcel.read(multipartFile.getInputStream(),VocabuDto.class,new DemoDataListener<Vocabu,VocabuDto,String>(vocabuMongoRepository,Vocabu.class)).sheet().doRead();
        return ResponseData.SUCCESS;
    }
}
