package com.example.handler;

import com.example.vo.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice//对价了@Controller注解的方法进行拦截处理 AOP实现
public class AllExceptionHandler {
    //进行异常的处理,处理Exception.class(几乎所有的异常)
        @ExceptionHandler(Exception.class)
        @ResponseBody//返回我们的json数据
    public Result doException(Exception ex){
            ex.printStackTrace();
            return Result.fail(-999,"系统异常");

        }

}
