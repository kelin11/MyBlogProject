package com.example.vo.params;

import com.example.service.SysUserService;
import lombok.Data;

@Data
public class LoginParam {
    private String account;
    private String password;
    private String nickname;


}
