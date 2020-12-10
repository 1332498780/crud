package com.cn.tfe.controller;

import com.cn.tfe.entity.User;
import com.cn.tfe.exception.CustomException;
import com.cn.tfe.filter.PassToken;
import com.cn.tfe.filter.UserLoginToken;
import com.cn.tfe.repository.UserRepository;
import com.cn.tfe.util.JwtUtil;
import com.cn.tfe.util.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserRepository userRepository;

    @PassToken
    @PostMapping("/login")
    public ResponseData<String> login(@RequestBody User user){
        User dbUser = userRepository.findUsersByUsername(user.getUsername());
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
    @PostMapping("/add")
    public ResponseData createUser(@RequestBody @Valid User user, Errors errors){
        if(errors.hasErrors()){
            throw new CustomException(errors.getAllErrors().get(0).getDefaultMessage());
        }
        userRepository.save(user);
        return ResponseData.SUCCESS;
    }
}
