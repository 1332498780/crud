package com.cn.tfe.excel;

import com.alibaba.excel.EasyExcel;
import com.cn.tfe.demo.Student;
import com.cn.tfe.dto.VocabuDto;
import com.cn.tfe.entity.Vocabu;
import com.cn.tfe.util.easypoi.DemoDataListener;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadTest {

    private static final Logger log = LoggerFactory.getLogger(ReadTest.class);

    @Test
    public void simpleRead(){
        String path = "C:\\Users\\hzy\\Desktop\\read.xlsx";
        EasyExcel.read(path, Student.class,new DemoDataListener<Student,Integer>()).sheet().doRead();
    }

    @Test
    public void readVocabu() throws IllegalAccessException, InstantiationException {
        String path = "C:\\Users\\hzy\\Desktop\\write.xlsx";
        EasyExcel.read(path, VocabuDto.class,new DemoDataListener<VocabuDto,Integer>()).sheet().doRead();
    }
}
