package com.cn.tfe;

import com.cn.tfe.entity.Vocabu;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;


public class AnnoTest {

    private static final Logger log = LoggerFactory.getLogger(AnnoTest.class);

    @Test
    public void  lombokDefaultTest(){

        Vocabu vocabu = new Vocabu();
        log.info(vocabu.toString());

        Vocabu vocabu1 = Vocabu.builder().build();
        log.info(vocabu1.toString());
    }
}
