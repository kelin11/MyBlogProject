package com.example.controller;

import com.example.domain.SysUser;
import com.example.utils.UserThreadLocal;
import com.example.vo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class test {

    @GetMapping ("/test")
    public Result test1(){
        SysUser sysUser = UserThreadLocal.get();
        //获取到 sysUser 从Controller中获取的信息  同时相关的问题还有
        //在ThreadLocal不进行remove的情况下可能造成的内存泄漏问题 以及它的原因
        System.out.println(sysUser);
        return Result.success(null);
    }
}
