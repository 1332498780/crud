package com.cn.tfe.repository;

import com.cn.tfe.entity.User;
import com.cn.tfe.entity.Vocabu;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface VocabuMongoRepository extends MongoRepository<Vocabu,String>,CustomVocabuRepository {

//    @Query("{word: $0,'transRes.dst': $1}")

}
