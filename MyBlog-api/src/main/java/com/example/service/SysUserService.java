package com.example.service;

import com.example.domain.SysUser;
import com.example.vo.ArticleVo;
import com.example.vo.CommentVo;
import com.example.vo.Result;
import com.example.vo.UserVo;

import java.util.List;

public interface SysUserService {
    //查询作者是谁
    SysUser findUserById(Long ArticleId);

    SysUser findUser(String account, String password);

    /**
     * 根据token查询user信息
     * @param token
     * @return
     */
    Result getUserInfoByToken(String token);

    /**
     * 根据register 的 account 找是否已经有相同的Account
     * @param account
     * @return
     */
    SysUser findUserByAccount(String account);

    /**
     * 保存用户信息
     * @param sysUser
     */
    void Save(SysUser sysUser);

    /**
     * 返回作者信息
     * @param AuthorId
     * @return
     */
    UserVo findAuthorById(Long AuthorId);



}
