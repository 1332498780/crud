package com.cn.tfe.repository;

import com.cn.tfe.entity.Vocabu;
import com.cn.tfe.util.ResponsePage;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

public interface CustomVocabuRepository {
    ResponsePage<List<Vocabu>> findByWordAndDict(String word, String dst, int page, int size);

    List<Vocabu> findByExampleWord(String word);

    void updateVocabu(String id,Vocabu vocabu);
}
