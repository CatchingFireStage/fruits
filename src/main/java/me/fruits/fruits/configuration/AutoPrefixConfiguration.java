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
        configurer.addPathPrefix("/admin", HandlerTypePredicate.forBasePackage("me.fruits.fruits.controller.admin"));
        configurer.addPathPrefix("/api", HandlerTypePredicate.forBasePackage("me.fruits.fruits.controller.api"));
    }

}
