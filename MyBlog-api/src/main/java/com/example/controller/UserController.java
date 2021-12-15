package com.example.controller;


import com.example.service.SysUserService;
import com.example.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private SysUserService sysUserService;

    /**
     * 获取当前的user的信息
     * @param token
     * @return
     */
    @GetMapping("/currentUser")
    public Result currentUser(@RequestHeader("Authorization") String token) {//前端也就是客户 每次访问都会接受这个参数 token 相当于来拿着钥匙找我们
        return  sysUserService.getUserInfoByToken(token);
    }

}

