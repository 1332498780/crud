package com.cn.tfe.controller;

import com.cn.tfe.anntations.LogAnno;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class TestController {

    @LogAnno(operateType="测试")
    @RequestMapping("/home")
    public @ResponseBody Map<String,String> home(){
        Map map = new HashMap<String,String>();
        map.put("name","zs");
        return map;
    }
}
