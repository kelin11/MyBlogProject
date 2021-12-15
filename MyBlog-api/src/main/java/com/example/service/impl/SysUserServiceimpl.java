package com.example.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.dao.SysUserDao;
import com.example.domain.Comment;
import com.example.domain.SysUser;
import com.example.service.LoginService;
import com.example.service.SysUserService;
import com.example.utils.JWTUtils;
import com.example.vo.*;
import org.apache.catalina.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SysUserServiceimpl implements SysUserService {

    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private LoginService loginService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    /**
     * 根据Article查询 AuthorName
     */
    public SysUser findUserById(Long authorId) {

        SysUser sysUser = sysUserDao.selectById(authorId);
        if(sysUser==null){
          sysUser = new SysUser();
          sysUser.setId(1L);
          sysUser.setNickname("unKnown_user");
        }
        return sysUser;
    }

    /**
     * 登录功能
     * @param account
     * @param password
     * @return
     */
    @Override
    public SysUser findUser(String account, String password) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account).eq(SysUser::getPassword,password)
                //select 是为了筛选出我们需要的SysUser中的信息
                .select(SysUser::getAccount,SysUser::getAvatar,SysUser::getId,SysUser::getNickname)
                //加快查询效率
                .last("limit 1");
        return sysUserDao.selectOne(queryWrapper);
    }

    @Override
    public Result getUserInfoByToken(String token) {
       //1.token合法进行校验
            //是否解析成功？ redis是否存在？
        //2.如果校验失败 返回错误 成功，就返回结果LoginUserVo
        if(token==null){
            return Result.fail(ErrorCode.TOKEN_ERROR.getCode(), ErrorCode.TOKEN_ERROR.getMsg());
        }
        //校验成功
        Map<String, Object> stringObjectMap = JWTUtils.checkToken(token);
        if(stringObjectMap==null){
            return Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
        }
        //在redis中查询看有没有:
        String UserJson = redisTemplate.opsForValue().get("TOKEN_" + token);
        if(StringUtils.isBlank(UserJson)){
            //用户登录过期了
            return Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
        }
        //将json数据解析成user对象
        SysUser sysUser = JSON.parseObject(UserJson, SysUser.class);
        LoginUserVo loginUserVo = new LoginUserVo();
        loginUserVo.setNickname(sysUser.getNickname());
        loginUserVo.setAccount(sysUser.getAccount());
        loginUserVo.setAvatar(sysUser.getAvatar());
        loginUserVo.setId(sysUser.getId());
        return Result.success(loginUserVo);
    }

    @Override
    public SysUser findUserByAccount(String account) {
        LambdaQueryWrapper<SysUser> queryWrapper  = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account).last("limit 1");
        return   sysUserDao.selectOne(queryWrapper);

    }

    /**
     * 保存操作
     * @param sysUser
     */
    @Override
    public void Save(SysUser sysUser) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        //注意 默认生成的id 是分布式id 采用了雪花算法
        sysUserDao.insert(sysUser);

    }

    /**
     * find User 信息 通过id (评论列表)
     * @param AuthorId
     * @return
     */
    @Override
    public UserVo findAuthorById(Long AuthorId) {
        SysUser sysUser = sysUserDao.selectById(AuthorId);
        if(sysUser==null){
            sysUser = new SysUser();
            sysUser.setId(1L);
            sysUser.setNickname("unKnown_user");
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(sysUser,userVo);
        return userVo;
    }
}
