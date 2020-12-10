package com.cn.tfe.repository;

import com.cn.tfe.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserMongoRepository extends MongoRepository<User,Integer> {
}
