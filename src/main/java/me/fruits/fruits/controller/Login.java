package me.fruits.fruits.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//作用在类或者接口上
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Login {
    //接口是否需要登录
    boolean isNeedLogin() default true;
}
