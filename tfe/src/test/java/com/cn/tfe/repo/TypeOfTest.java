package com.cn.tfe.repo;

import com.cn.tfe.ChineseTest;
import com.cn.tfe.dto.VocabuDto;
import com.cn.tfe.entity.Vocabu;
import com.cn.tfe.repository.VocabuMongoRepository;
import com.cn.tfe.util.easypoi.DemoDataListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TypeOfTest {

    Logger log = LoggerFactory.getLogger(TypeOfTest.class);

    @Autowired
    VocabuMongoRepository vocabuMongoRepository;

    @Test
    public void testType() throws IllegalAccessException, InstantiationException {
//       Type type = vocabuMongoRepository.getClass().getGenericSuperclass();
//        ParameterizedType parameterizedType = (ParameterizedType)type;
//        log.info(parameterizedType.getTypeName());

    }
}
