package com.example.controller;


import com.example.service.LoginService;
import com.example.vo.Result;
import com.example.vo.params.LoginParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
public class RegisterController {
    @Autowired
    private LoginService loginService;


    @PostMapping
    public Result regist(@RequestBody LoginParam loginParam){
        return loginService.regist(loginParam);
    }

    
}
