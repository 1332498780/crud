package com.cn.tfe.controller;

import com.cn.tfe.entity.Vocabu;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/vocabu")
public class VocabuController {

    private static final Logger log = LoggerFactory.getLogger(VocabuController.class);

    @Autowired
    private VocabuMongoRepository vocabuMongoRepository;

    @PostMapping("/page")
    public ResponseData<List<Vocabu>> getPageList(@RequestBody RequestPageData<VocabuFilterParam> requestPageData){
        if(requestPageData.getData()==null){
            Page<Vocabu> pageData = vocabuMongoRepository.findAll(requestPageData.pgData());
            return ResponsePage.<Vocabu>page(pageData);
        }
        VocabuFilterParam param = requestPageData.getData();
        ExampleMatcher matcher = ExampleMatcher.matching();
        if(!StringUtils.isEmpty(param.getWord())){
            matcher.withMatcher("word",ExampleMatcher.GenericPropertyMatchers.contains());
        }
        if(!StringUtils.isEmpty(param.getDst())){
            matcher.withMatcher("dst",ExampleMatcher.GenericPropertyMatchers.contains());
        }
        return ResponsePage.<Vocabu>page(null);
    }

}
