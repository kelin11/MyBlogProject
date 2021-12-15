package com.example.controller;


import com.example.service.LoginService;
import com.example.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logout")
public class Logoutcontroller {
    @Autowired
    private LoginService loginService;


    @GetMapping
    public Result logout(@RequestHeader("Authorization") String token){
        //返回的`data是 token key
        return loginService.logout(token);
    }
}
