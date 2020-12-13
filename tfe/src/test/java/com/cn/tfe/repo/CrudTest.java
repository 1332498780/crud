package com.cn.tfe.repo;


import com.cn.tfe.entity.Vocabu;
import com.cn.tfe.entity.VocabuTrans;
import com.cn.tfe.repository.VocabuMongoRepository;
import com.cn.tfe.repository.VocabuTransRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CrudTest {

    private static final Logger log = LoggerFactory.getLogger(CrudTest.class);

    @Autowired
    VocabuMongoRepository vocabuMongoRepository;

    @Autowired
    VocabuTransRepository vocabuTransRepository;

    @Test
    public void testInsert(){
//                .dst("煎饼")
//                .phonetic("jian bing")
//                .eDict("a thin flat round cake made from a mixture of flour")
//                .strokes(Arrays.asList(new String[]{"http://www.audio1.com","http://www.audio2.com"}))
//                .volumnUrl("http://www.img.com")
//                .build();
//        CommonRes cr1 = CommonRes.builder()
//                .dst("煎饼侠")
//                .word("pancake man")
//                .phonetic("jian bing xia")
//                .build();
//        List<CommonRes> wordList = new ArrayList();
//        wordList.add(cr1);
//
//        CommonRes cr2 = CommonRes.builder()
//                .dst("这个煎饼真好吃")
//                .word("This pancake is delicious")
//                .phonetic("zhe ge jian bing zhen hao chi")
//                .build();
//        List<CommonRes> senList = new ArrayList();
//        senList.add(cr2);
//
//        Vocabu vo =  Vocabu.builder()
//                .id("1")
//                .title("dog title")
//                .description("dog desc")
//                .word("dog1")
//                .transRes(tr1)
//                .exampleRes(wordList)
//                .similarRes(senList)
//                .build();
//        log.info(vo.toString());
//        Vocabu newVo = vocabuMongoRepository.insert(vo);
//        log.info("新:"+newVo.toString());
    }

    @Test
    public void testTranslate(){
//        List<String> lists = Arrays.asList(new String[]{"5fd45d6c98013e6cd4c3ccb8","5fd460b7c6b6ba04ca3af64d"});
//        long res = vocabuMongoRepository.updateTranslate(lists);
//        log.info(String.valueOf(res));
    }

    @Test
    public void testTransInsert(){
//        VocabuTrans vocabuTrans = VocabuTrans.builder().word("test").fromTo(13).build();
//        vocabuTransRepository.insert(vocabuTrans);
    }

}
