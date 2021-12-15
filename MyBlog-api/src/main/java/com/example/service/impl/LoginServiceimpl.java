package com.example.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.dao.SysUserDao;
import com.example.domain.SysUser;
import com.example.service.LoginService;
import com.example.service.SysUserService;
import com.example.utils.JWTUtils;
import com.example.vo.ErrorCode;
import com.example.vo.Result;
import com.example.vo.params.LoginParam;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceimpl implements LoginService {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private SysUserDao sysUserDao;


    //md5 生产密码 存放加密salt
    private static final String salt = "mszlu!@#";

    /**
     * 注册功能
     * @param loginParam
     * @return
     */
    @Override
    public Result login(LoginParam loginParam) {

        /**
         * 1.先检查参数是否合法
         * 2.根据user和 密码 在user表中查询 是否存在
         * 3.如果不存在 登陆失败
         * 4.如果存在 使用jwt技术 生成token 返回给前端
         * 5.token 放入redis当中  token:user 信息 设置过期时间(登录认证的时候 先认证 token 去redis认证它是否存在再去数据库)
         */
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        //1.完成检查参数是否合法 这个方法就是isBlank 空格也算空的
        if(StringUtils.isBlank(account)||(StringUtils.isBlank(password))){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        //加密
        String pwd = DigestUtils.md5Hex(password+salt);
        SysUser sysUser = sysUserService.findUser(account,pwd);
        if(sysUser==null){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        //存在  生成token   "key"
        String token = JWTUtils.createToken(sysUser.getId());
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);//同时设置过期时间   JWT的  C部分
        //使用redis存放C部分(token) 并且返回给前端
        return Result.success(token);
    }

    /**
     * 退出功能
     * @return
     */
    @Override
    public Result logout(String token) {
        //直接把 redis中的 token删除就可以
        redisTemplate.delete("TOKEN_"+token);
        return Result.success(token);
    }

    /**
     * 注册功能
     * @param loginParam
     * @return
     */
    @Override
    public Result regist(LoginParam loginParam) {
        //判断 registaccount 和 password nickname是否合法?
        //判断 account是否存在 存在就返回"账户已经注册了"
        //如果账户不存在 注册用户 添加用户 生成token 生成token 转入 redis 并且返回token data
        //同时 一定要注意 : 注册的过程中 要添加上回滚操作 一旦注册中间出现任何的问题 都直接回滚操作 不让数据进入我们的数据库
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        String nickname = loginParam.getNickname();
        if(StringUtils.isBlank(account)||StringUtils.isBlank(password)||StringUtils.isBlank(nickname)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        SysUser sysUser = this.sysUserService.findUserByAccount(account);
        if(sysUser!=null){
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(), ErrorCode.ACCOUNT_EXIST.getMsg());
        }
        sysUser = new SysUser();
        sysUser.setAccount(account);
        sysUser.setNickname(nickname);
        sysUser.setPassword(DigestUtils.md5Hex(password+salt));
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUser.setAdmin(1); //1 为true
        sysUser.setDeleted(0); // 0 为false
        sysUser.setSalt("");
        sysUser.setStatus("");
        sysUser.setEmail("");
        this.sysUserService.Save(sysUser);

        String token = JWTUtils.createToken(sysUser.getId());
        //将token 放入redis中去
        redisTemplate.opsForValue().set("TOKEN_"+token,JSON.toJSONString(sysUser),1,TimeUnit.DAYS);
        return Result.success(token);
    }

    @Override
    public SysUser checkToken(String token) {
        Map<String, Object> map = JWTUtils.checkToken(token);
        if (map == null) {
            return null;
        }
        String UserJson = redisTemplate.opsForValue().get("TOKEN_" + token);
        SysUser sysUser = JSON.parseObject(UserJson, SysUser.class);
        if (sysUser == null) {

            return null;
        }
        return sysUser;
    }



}

