package com.example.service;

import com.example.domain.SysUser;
import com.example.vo.LoginUserVo;
import com.example.vo.Result;
import com.example.vo.params.LoginParam;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface LoginService {

    /**
     * 登录功能
     * @param loginParam
     * @return
     */
    Result login(LoginParam loginParam);

    Result logout(String token);

    Result regist(LoginParam loginParam);

    SysUser checkToken(String token);


}
