package com.cn.tfe.repo;

import com.cn.tfe.ChineseTest;
import com.cn.tfe.dto.VocabuDto;
import com.cn.tfe.entity.Vocabu;
import com.cn.tfe.util.easypoi.DemoDataListener;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;

public class TypeOfTest {

    Logger log = LoggerFactory.getLogger(TypeOfTest.class);

    @Test
    public void testType() throws IllegalAccessException, InstantiationException {
        DemoDataListener demo = new DemoDataListener<Vocabu, VocabuDto,String>();

        Type types = demo.getClass().getGenericSuperclass();
//        for(Type t:types){
            log.info(types.getTypeName());
//        }

    }
}
