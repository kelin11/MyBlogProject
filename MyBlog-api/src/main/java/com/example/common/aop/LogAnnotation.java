package com.example.common.aop;

import org.springframework.stereotype.Service;

import java.lang.annotation.*;

@Target({ElementType.TYPE,ElementType.METHOD})
//type代表可以放在类的上面 method代表可以放在方法上
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {
    String module() default "";
    String operator() default "";

}
