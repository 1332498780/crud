package com.cn.tfe.controller;

import com.cn.tfe.entity.Vocabu;
import com.cn.tfe.exception.CustomException;
import com.cn.tfe.query.VocabuFilterParam;
import com.cn.tfe.repository.VocabuMongoRepository;
import com.cn.tfe.util.RequestPageData;
import com.cn.tfe.util.ResponseData;
import com.cn.tfe.util.ResponsePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value="/vocabu")
public class VocabuController {

    private static final Logger log = LoggerFactory.getLogger(VocabuController.class);

    @Autowired
    VocabuMongoRepository vocabuMongoRepository;

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
            Vocabu savedVocabu = vocabuMongoRepository.save(vocabu);
            if(savedVocabu!=null){
                return ResponseData.SUCCESS;
            }
        }
        throw new CustomException("更新单词失败");
    }

    @PostMapping("/upload")
    public void uploadFile(@RequestBody MultipartFile multipartFile){
        String fileName = multipartFile.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf('.')+1);
        if(!suffix.equals("xls") && !suffix.equals("xlsx")){
            throw new CustomException("文件格式有误，请使用xls/xlsx格式文件");
        }





    }

}
