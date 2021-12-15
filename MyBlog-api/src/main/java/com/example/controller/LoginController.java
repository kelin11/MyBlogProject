package com.example.controller;

import com.example.service.LoginService;
import com.example.vo.Result;
import com.example.vo.params.LoginParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping
    public Result login(@RequestBody LoginParam loginParam){
        //验证用户: 要访问user表  通过验证返回来的loginparam来验证user
        return  loginService.login(loginParam);
    }
    
}
