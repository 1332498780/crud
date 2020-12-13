package com.cn.tfe.repository;

import com.cn.tfe.entity.Vocabu;
import com.cn.tfe.entity.VocabuTrans;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VocabuTransRepository extends MongoRepository<VocabuTrans,String>,CustomVocabuRepository {
}
