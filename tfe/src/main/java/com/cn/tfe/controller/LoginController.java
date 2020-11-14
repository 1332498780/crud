package com.cn.tfe.controller;

import com.cn.tfe.entity.User;
import com.cn.tfe.exception.CustomException;
import com.cn.tfe.filter.UserLoginToken;
import com.cn.tfe.service.UserService;
import com.cn.tfe.util.JwtUtil;
import com.cn.tfe.util.ResponseData;
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
    public ResponseData<String> login(@RequestBody User user){
        User dbUser = userService.findUserByUsername(user.getUsername());
        Map<String,String> map = new HashMap();
        if(dbUser!=null){
            if(dbUser.getPassword().equals(user.getPassword())){
                String token = JwtUtil.getToken(user);
                return ResponseData.of(token);
            }else{
                throw new CustomException("密码错误");
            }
        }else{
            throw new CustomException("用户名错误");
        }
    }

    @UserLoginToken
    @GetMapping("/getMessage")
    public ResponseData<User> getMessage(){
        return ResponseData.of(new User(1,"zs","123"));
    }
}
