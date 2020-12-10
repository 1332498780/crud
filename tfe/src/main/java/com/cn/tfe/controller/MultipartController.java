package com.cn.tfe.controller;

import com.cn.tfe.exception.CustomException;
import com.cn.tfe.filter.UserLoginToken;
import com.cn.tfe.util.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@UserLoginToken
public class MultipartController {

    private static final Logger log = LoggerFactory.getLogger(MultipartController.class);

    private String uploadPath = "/tmp";

    private final Long B_SIZE = 1l;
    private final Long MB_SIZE = 1024 * 1024 * B_SIZE;


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
//        if(docIndex == -1
//                || (!(suffix=originName.substring(docIndex)).equals(".jpg") && ! suffix.equals(".png")))
        if(docIndex == -1
                || (!suffixValidation(suffix=originName.substring(docIndex),".jpg",".png",".jpeg")))
            throw new CustomException("请上传正确格式的文件");
        long byteSize = multipartFile.getSize();  //字节单位
        if(byteSize > MB_SIZE){
            throw new CustomException("禁止上传文件大小超过1M");
        }
        String newName = saveToLocal(suffix,multipartFile);
        return ResponseData.of(newName);
    }


    @PostMapping("/uploadMp4")
    @ResponseBody
    public ResponseData<String> uploadMp4(@RequestPart("file") MultipartFile multipartFile){
        String originName = multipartFile.getOriginalFilename();
        int docIndex = originName.lastIndexOf('.');
        String suffix = "";
        if(docIndex == -1
                || (!suffixValidation(suffix=originName.substring(docIndex),".mp4")))
            throw new CustomException("请上传正确格式的文件");
        long byteSize = multipartFile.getSize();  //字节单位
        if(byteSize > MB_SIZE){
            throw new CustomException("禁止上传文件大小超过1M");
        }
        String newName = saveToLocal(suffix,multipartFile);
        return ResponseData.of(newName);
    }


    /***
     * 把文件保存在服务器本地目录并返回新的文件名
     * @param suffix 以 .开头; ep: .jpg .png
     * @param multipartFile
     * @return
     */
    private String saveToLocal(String suffix,MultipartFile multipartFile){
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
        return newName;
    }

    /***
     * 格式校验，命中一个格式即可
     * @param suffix
     * @param validSuffix
     * @return
     */
    private boolean suffixValidation(String suffix,String ...validSuffix){
        for(String s:validSuffix){
            if(s.equals(suffix)){
                return true;
            }
        }
        return false;
    }
}
