package com.example.controller;

import com.example.utils.QiniuUtils;
import com.example.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    private QiniuUtils qiniuUtils;

    @PostMapping
    public Result upload(@RequestParam("image")MultipartFile file) throws IOException {
        //获取上传上来的 原始文件的名称
        String originalFilename =  file.getOriginalFilename();
        //前面的是一个随机的一个字符串  得到的fileName 惟一的文件的名称
        String fileName = UUID.randomUUID().toString() + "." + StringUtils.substringAfterLast(originalFilename, ".");
        //上传文件 上传到哪里?
        //首先 不建议上传到自己的服务器!
        boolean upload = qiniuUtils.upload(file,fileName);
        if(upload){
            return Result.success(QiniuUtils.url+fileName);
        }
        return Result.fail(20001,"上传失败");
    }



}
