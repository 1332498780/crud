package com.cn.tfe.repository;

import com.cn.tfe.entity.Vocabu;
import com.cn.tfe.util.ResponsePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;

import java.util.List;

public class CustomVocabuRepositoryImpl implements CustomVocabuRepository{

    @Autowired
    MongoOperations mongo;

    @Override
    public ResponsePage<List<Vocabu>> findByWordAndDict(String word, String dst, int page, int size) {
        dst = "/.*"+dst+".*/";
        long skip = 0l;
        if(page > 0){
            skip = size*page;
        }
        Criteria criteria = null;
        if(!StringUtils.isEmpty(word)){
            criteria = Criteria.where("word").is(word);
        }
        if(!StringUtils.isEmpty(dst)){
            if(criteria == null){
                criteria = Criteria.where("\"transRes.dst\"").regex(dst);
            }else{
                criteria = criteria.and("\"transRes.dst\"").regex(dst);
            }
        }
        Query queryCount = new Query(criteria);
        Query queryData = new Query(criteria).limit(size).skip(skip);
        long total = mongo.count(queryCount,Vocabu.class);
        List<Vocabu> data = mongo.find(queryData,Vocabu.class);
        return ResponsePage.page(data,total,size);
    }
}
