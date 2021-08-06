package me.fruits.fruits.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * mini模块认证拦截器
 */
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    /**
     * Controller方法调用前执行
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("{}",request.getRequestURI());
        if(request.getRequestURI().indexOf("/admin/") == 0){
            //todo admin拦截
            return true;
        }

        return false;
    }

    /**
     * Controller方法正常返回后执行
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 无论Controller方法是否抛异常都会执行
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
