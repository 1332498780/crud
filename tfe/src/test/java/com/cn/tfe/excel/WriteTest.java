package com.cn.tfe.excel;

import com.alibaba.excel.EasyExcel;
import com.cn.tfe.dto.VocabuDto;
import com.cn.tfe.entity.CommonRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class WriteTest {

    private static final Logger log = LoggerFactory.getLogger(WriteTest.class);

//    @Test
    public void simpleRead(){

//        Gson gson = new Gson();
//        log.info(gson.toJson(CommonRes.builder().dst("小狗").dstTts("http://").phonetic("xiao gou").build()));
//        List<CommonRes> list = new ArrayList<>();
//        list.add(CommonRes.builder().dst("小狗").dstTts("http://").phonetic("xiao gou").build());
//        list.add(CommonRes.builder().dst("小狗").dstTts("http://").phonetic("xiao gou").build());
//        log.info(gson.toJson(list));


        String path = "C:\\Users\\hzy\\Desktop\\write.xlsx";
        VocabuDto vocabuDto = VocabuDto.builder()
                .word("dog")
                .edict("dog is a animal")
                .transRes(CommonRes.builder().dst("狗").dstTts("http://").phonetic("gou").build())
                .exampleRes(Arrays.asList(new CommonRes[]{
                        CommonRes.builder().dst("小狗").dstTts("http://").phonetic("xiao gou").word("paddy").build()
                }))
                .similarRes(Arrays.asList(new CommonRes[]{
                        CommonRes.builder().dst("小狗很可爱").dstTts("http://").phonetic("xiao gou").word("Puppy is cute").build()
                }))
                .build();
        log.info(vocabuDto.toString());
        EasyExcel.write(path,VocabuDto.class).sheet("模板").doWrite(Arrays.asList(new VocabuDto[]{vocabuDto}));
    }
}
