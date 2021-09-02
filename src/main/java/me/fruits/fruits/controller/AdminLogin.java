package me.fruits.fruits.controller;

import java.lang.annotation.*;

/**
 * 接口认证
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Login
public @interface AdminLogin {
//    //admin模块的接口是否需要登录
//    boolean isNeedLogin() default true;
}
