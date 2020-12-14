package com.cn.tfe.repository;

import com.cn.tfe.entity.Vocabu;
import com.cn.tfe.util.ResponsePage;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.internal.bulk.UpdateRequest;
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
    public ResponsePage<List<Vocabu>> findByWordAndDict(String word, String dst, int page, int size) {
        long skip = 0l;
        if(page > 0){
            skip = size*page;
        }
        Criteria criteria = null;
        if(!StringUtils.isEmpty(word)){
            criteria = Criteria.where("word").is(word);
        }
        if(!StringUtils.isEmpty(dst)){
            dst = ".*"+dst+".*";
            if(criteria == null){
                criteria = Criteria.where("transRes.dst").regex(Pattern.compile(dst));
            }else{
                criteria = criteria.and("transRes.dst").regex(Pattern.compile(dst));
            }
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

    @Override
    public long updateTranslate(String[] words) {
        Criteria criteria = Criteria.where("word").in(words).and("isTranslate").is(0);
        Query query = new Query(criteria);
        Update update = Update.update("isTranslate",1);
        UpdateResult result = mongo.updateMulti(query,update,Vocabu.class);
        return result.getModifiedCount();
    }

    @Override
    public List<Vocabu> findTranslateBaseCount(int count) {
        Criteria criteria = Criteria.where("isTranslate").is(0);
        Query query = new Query(criteria).limit(count);
//        query.fields().include("_id").include("word");
        List<Vocabu> vocabus =  mongo.find(query,Vocabu.class);
        return vocabus;
    }
}
