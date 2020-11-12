package com.cn.tfe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class TestController {

    @RequestMapping("/home")
    public @ResponseBody Map<String,String> home(){
        Map map = new HashMap<String,String>();
        map.put("name","zs");
        return map;
    }
}
