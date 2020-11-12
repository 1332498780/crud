package com.cn.tfe.controller;

import com.cn.tfe.entity.User;
import com.cn.tfe.filter.UserLoginToken;
import com.cn.tfe.service.UserService;
import com.cn.tfe.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public Object login(@RequestBody User user){
        User dbUser = userService.findUserByUsername(user.getUsername());
        Map<String,String> map = new HashMap();
        if(dbUser!=null){
            if(dbUser.getPassword().equals(user.getPassword())){
                String token = JwtUtil.getToken(user);
                map.put("token",token);
                return map;
            }else{
                map.put("msg","密码错误");
            }
        }else{
            map.put("msg","用户名错误");
        }
        return map;
    }

    @UserLoginToken
    @GetMapping("/getMessage")
    public String getMessage(){
        return "你已通过验证";
    }
}
