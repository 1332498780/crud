package com.cn.tfe;

import org.junit.Test;

public class RegexText {

    @Test
    public void regexTest(){

        String str = "i .like ,you 123! 中文";
        String res = str.toLowerCase().trim().replaceAll("[^a-z|\\s]*","");
        System.out.println(res);
    }


    @Test
    public void chineseTest(){
        String str = "123 짐작 하 다 abc!";
        String res = str.replaceAll("","").trim();
        System.out.println(res);
    }
}
