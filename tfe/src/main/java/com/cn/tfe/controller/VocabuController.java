package com.cn.tfe.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.cn.tfe.dto.VocabuDto;
import com.cn.tfe.emums.Language;
import com.cn.tfe.entity.CommonRes;
import com.cn.tfe.entity.Vocabu;
import com.cn.tfe.exception.CustomException;
import com.cn.tfe.filter.PassToken;
import com.cn.tfe.filter.UserLoginToken;
import com.cn.tfe.query.VocabuFilterParam;
import com.cn.tfe.repository.VocabuMongoRepository;
import com.cn.tfe.service.AsynVocabuService;
import com.cn.tfe.service.SynHandleData;
import com.cn.tfe.util.RequestPageData;
import com.cn.tfe.util.ResponseData;
import com.cn.tfe.util.ResponsePage;
import com.cn.tfe.util.baidu.TransApi;
import com.cn.tfe.util.easypoi.DemoDataListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

@RestController
@RequestMapping(value="/vocabu")
@UserLoginToken
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

    @PassToken
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

    @PassToken
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

    @PassToken
    @GetMapping("getTemplate")
    public void getRemplate(@RequestParam("param") String  requestParam,
        HttpServletResponse response
    ) throws IOException {
        long startTime = System.currentTimeMillis();
        log.info("writeBatch start >>> "+startTime);

        String[] params = requestParam.split(",");
        List<String> paramList = Arrays.asList(params);
        Map<String,List<String>> map = new HashMap<>();
        map.put("param",paramList);

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("导入模板", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), VocabuDto.class).autoCloseStream(Boolean.FALSE).build();
        WriteSheet writeSheet = EasyExcel.writerSheet("模板").build();
        try {
            preHandle(map, data -> {
                List<VocabuDto> list = new ArrayList<>(data.size());
                for (Object obj : data) {
                    Vocabu vocabu = (Vocabu) obj;
                    VocabuDto vocabuDto = new VocabuDto();
                    BeanUtils.copyProperties(vocabu, vocabuDto);
                    list.add(vocabuDto);
                }
                excelWriter.write(list, writeSheet);
            });
        }catch (Exception e){
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().println("{\"code\":500,data:\""+e.getMessage()+"\"}");
        }finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
            long endTime = System.currentTimeMillis();
            log.info("writeBatch end >>> "+endTime);
            log.info("totally cost "+((endTime - startTime)/1000) +" s ");
        }
    }


    @PostMapping("getTemplate")
    public void getRemplate(@RequestBody Map<String,List<String>>  requestParam,
                            HttpServletResponse response
    ) throws IOException {
        long startTime = System.currentTimeMillis();
        log.info("writeBatch start >>> "+startTime);

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("导入模板", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), VocabuDto.class).autoCloseStream(Boolean.FALSE).build();
        WriteSheet writeSheet = EasyExcel.writerSheet("模板").build();
        try {
            preHandle(requestParam, data -> {
                List<VocabuDto> list = new ArrayList<>(data.size());
                for (Object obj : data) {
                    Vocabu vocabu = (Vocabu) obj;
                    VocabuDto vocabuDto = new VocabuDto();
                    BeanUtils.copyProperties(vocabu, vocabuDto);
                    list.add(vocabuDto);
                }
                excelWriter.write(list, writeSheet);
            });
        }catch (Exception e){
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().println("{\"code\":500,data:\""+e.getMessage()+"\"}");
        }finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
            long endTime = System.currentTimeMillis();
            log.info("writeBatch end >>> "+endTime);
            log.info("totally cost "+((endTime - startTime)/1000) +" s ");
        }
    }

    @PostMapping("writeBatch")
    public ResponseData writeBatch(@RequestBody Map<String,List<String>>  requestParam,
                                         HttpServletResponse response
    ) throws IOException {
        long startTime = System.currentTimeMillis();
        log.info("writeBatch start >>> "+startTime);

        preHandle(requestParam, data -> {
            vocabuMongoRepository.insert(data);
        });
        long endTime = System.currentTimeMillis();
        log.info("writeBatch end >>> "+endTime);
        log.info("totally cost "+((endTime - startTime)/1000) +" s ");
        return ResponseData.SUCCESS;
    }

    private void preHandle(Map<String,List<String>> requestParam, SynHandleData synHandleData){

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
                                synHandleData.hanldeData(vocabus);
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
                synHandleData.hanldeData(vocabus);
//                excelWriter.write(vocabuDtos, writeSheet);
            }
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
//        String regexStr = "^[a-z]{1,45}$";
        for(String word:source){
            String lowwer = word.trim().toLowerCase();
            String replaceStr = lowwer.replaceAll("[^a-z|\\s]*","");
            if(replaceStr.length()>0){
                res.add(lowwer);
            }else{
                count++;
                sb.append(word).append('\n');
            }
//            if(lowwer.matches(regexStr)){
//                res.add(lowwer);
//            }else{
//                count++;
//                sb.append(word).append(",");
//            }
        }
//        if(sb.toString().endsWith(",")){
//            // 注意不能写成 sb.replace(len-1,len-1,"]") 头尾相等的话，会多添加一个"]"在","之前
//            sb.replace(sb.length()-1,sb.length(),"]");
//        }
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
