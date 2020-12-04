package com.cn.tfe.repository;

import com.cn.tfe.entity.Vocabu;
import com.cn.tfe.util.ResponsePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.regex.Pattern;

public class CustomVocabuRepositoryImpl implements CustomVocabuRepository{

    @Autowired
    MongoOperations mongo;

    @Override
    public ResponsePage<List<Vocabu>> findByWordAndDict(String word, String dst,int fromTo, int page, int size) {
        long skip = 0l;
        if(page > 0){
            skip = size*page;
        }
        Criteria criteria = Criteria.where("fromTo").is(fromTo);
        if(!StringUtils.isEmpty(word)){
            criteria = criteria.and("word").is(word);
        }
        if(!StringUtils.isEmpty(dst)){
            dst = ".*"+dst+".*";
            criteria = criteria.and("transRes.dst").regex(Pattern.compile(dst));

        }
        Query queryCount = new Query(criteria);
        Query queryData = new Query(criteria).limit(size).skip(skip);
        long total = mongo.count(queryCount,Vocabu.class);
        List<Vocabu> data = mongo.find(queryData,Vocabu.class);
        return ResponsePage.page(data,total,size);
    }

    @Override
    public List<Vocabu> findByExampleWord(String word){
        String likeWord = ".*"+word+".*";
        Criteria criteria = Criteria.where("word").ne(word).and("similarRes.word").regex(Pattern.compile(likeWord));
        List<Vocabu> data = mongo.find(new Query(criteria),Vocabu.class);
        return data;
    }

    @Override
    public void updateVocabu(String id, Vocabu vocabu) {
//        vocabu.setId(id);
//        Update u = new Update();
//        mongo.updateFirst()
    }
}
