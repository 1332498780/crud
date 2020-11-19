package com.cn.tfe.controller;

import com.cn.tfe.exception.CustomException;
import com.cn.tfe.util.ResponseData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/multi")
public class MultipartController {

    private String uploadPath = "/tmp";

    @Value("${spring.resources.static-locations}")
    public void setUploadPath(String configPath){
        String[] paths = configPath.split(",");
        if(paths.length > 0 && configPath.contains("file:")){
            for(String p : paths){
                if(p.startsWith("file:")){
                    this.uploadPath = p.substring(5);
                    break;
                }
            }
        }
    }

    @PostMapping("/uploadImg")
    @ResponseBody
    public ResponseData<String> uploadFile(@RequestPart("file") MultipartFile multipartFile){
        String originName = multipartFile.getOriginalFilename();
        int docIndex = originName.lastIndexOf('.');
        String suffix = "";
        if(docIndex == -1
                || (!(suffix=originName.substring(docIndex)).equals(".jpg") && ! suffix.equals(".png")))
            throw new CustomException("请上传正确格式的文件");
        File dir = new File(this.uploadPath);
        if(!dir.exists()){
            if(!dir.mkdirs()){
                throw new CustomException("创建存储目录失败！");
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssZ");
        String addition = sdf.format(new Date());
        String newName = addition + suffix;
        String filePath = this.uploadPath+"/"+newName;
        File newFile = new File(filePath);
        try {
            multipartFile.transferTo(newFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException("上传文件失败！");
        }
        return ResponseData.of(newName);
    }
}
