package com.cn.tfe.excel;

import com.alibaba.excel.EasyExcel;
import com.cn.tfe.demo.Student;
import com.cn.tfe.util.easypoi.DemoDataListener;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReadTest.class);

    @Test
    public void simpleRead(){
        String path = "C:\\Users\\hzy\\Desktop\\read.xlsx";
        EasyExcel.read(path, Student.class,new DemoDataListener<Student,Integer>()).sheet().doRead();
    }
}
