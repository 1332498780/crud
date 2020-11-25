package com.cn.tfe;

import com.sun.org.apache.xpath.internal.operations.String;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringTest {

    private static final Logger log = LoggerFactory.getLogger(StringTest.class);

    @Test
    public void sbTest(){
        StringBuilder sb = new StringBuilder("123,234,");
        if(sb.toString().endsWith(",")){
            int len = sb.length();
            log.info(len+"");
            sb.replace(len-1,len,"]");
        }
        log.info(sb.toString());
    }
}
