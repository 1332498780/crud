package com.cn.tfe.controller;

import com.cn.tfe.exception.CustomException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/multi")
public class MultipartController {

    @PostMapping("/upload")
    public void uploadFile(@RequestBody MultipartFile multipartFile){






    }
}
