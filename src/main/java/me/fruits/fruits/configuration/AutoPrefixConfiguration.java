package me.fruits.fruits.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AutoPrefixConfiguration implements WebMvcConfigurer {


    /**
     * 路由前缀
     *
     * @param configurer
     */
    public void configurePathMatch(PathMatchConfigurer configurer) {
        //后台
        configurer.addPathPrefix("/admin", HandlerTypePredicate.forBasePackage("me.fruits.fruits.controller.admin"));
        //前台
        configurer.addPathPrefix("/api", HandlerTypePredicate.forBasePackage("me.fruits.fruits.controller.api"));
        //三方通知
        configurer.addPathPrefix("/notify", HandlerTypePredicate.forBasePackage("me.fruits.fruits.controller.notify"));
    }

}
