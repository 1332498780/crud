package com.cn.tfe.repository;

import com.cn.tfe.entity.User;
import com.cn.tfe.entity.Vocabu;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface VocabuMongoRepository extends MongoRepository<Vocabu,String>,CustomVocabuRepository {

//    @Query(value="{ 'firstname' : ?0 }", fields="{ 'firstname' : 1, 'lastname' : 1}")
////    List<Vocabu> findByTr(String firstname);

}
