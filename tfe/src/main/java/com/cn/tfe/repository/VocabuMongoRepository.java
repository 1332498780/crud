package com.cn.tfe.repository;

import com.cn.tfe.entity.User;
import com.cn.tfe.entity.Vocabu;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VocabuMongoRepository extends MongoRepository<Vocabu,Integer> {
}
