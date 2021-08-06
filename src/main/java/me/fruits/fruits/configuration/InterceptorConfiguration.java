package me.fruits.fruits.configuration;

import me.fruits.fruits.controller.AuthInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {

    /**
     * 添加拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/swagger-ui/**")
                .excludePathPatterns("/swagger-resources/**")
                .excludePathPatterns("/v2/api-docs")
                .excludePathPatterns("/admin/assist/login");
    }

    @Bean
    public AuthInterceptor authInterceptor() {
        return new AuthInterceptor();
    }
}
