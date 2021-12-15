package com.example.handler;


import com.alibaba.fastjson.JSON;
import com.example.domain.SysUser;
import com.example.service.LoginService;
import com.example.utils.UserThreadLocal;
import com.example.vo.ErrorCode;
import com.example.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截器
 */
//@Component
@ControllerAdvice
@Slf4j
public class LoginIntercepter implements HandlerInterceptor {

    @Autowired
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //再执行controller方法之前执行handler
        /**
         * 1.需要进行判断 请求的接口路径是否为HandlerMethod（controller方法）
         * 2.判断token是否为空
         * 3.如果token不为空 进行登陆验证checktoken
         * 4.验证成功 放行
         */
        if(!(handler instanceof HandlerMethod)){
            //如果不是handler的方法 直接放行
            //handler可能是RequestResourceHandler 默认去访问 classpath下面的static 静态资源 这种我们就直接放行
            return true;
        }
        String token = request.getHeader("Authorization");
        log.info("=================request start===========================");
        String requestURI = request.getRequestURI();
        log.info("request uri:{}",requestURI);
        log.info("request method:{}",request.getMethod());
        log.info("token:{}", token);
        log.info("=================request end===========================");
        if(StringUtils.isBlank(token)){
            //让他去登陆
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), "您没有登录,请先进行登录");//因为我们返回的是json信息 所以要将它转换成JSON格式 因为这里返回值类型是Boolean 而不是Result的Json
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(JSON.toJSONString(result));
            response.sendRedirect("/login");
            return false;
        }
        //token不为空,不拦截,进行token的认证
            SysUser sysUser = loginService.checkToken(token);
            if(sysUser==null){
                Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), "您没有登录,请先进行登录");//因为我们返回的是json信息 所以要将它转换成JSON格式 因为这里返回值类型是Boolean 而不是Result的Json
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write(JSON.toJSONString(result));
                return false;

            }
            UserThreadLocal.put(sysUser);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserThreadLocal.remove();
    }
}
