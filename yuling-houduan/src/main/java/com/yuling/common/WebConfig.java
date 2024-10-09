package com.yuling.common;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    public WebConfig(JwtInterceptor jwtInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
    }
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
//        指的controller统一接口前缀
        configurer.addPathPrefix("/yuling",clazz -> clazz.isAnnotationPresent(RestController.class));
    }

    @Resource
    private JwtInterceptor jwtInterceptor;

    // 自定义拦截器 JwtInterceptor ， 设置拦截规则
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
         //拦截所有yuling开头的请求 ， 排除掉login接口与忘记密码接口
        registry.addInterceptor(jwtInterceptor).
                addPathPatterns("/yuling/**")
                .excludePathPatterns("/yuling/captcha")
                .excludePathPatterns("/yuling/files/getAvatar/**")
                .excludePathPatterns("/yuling/user/login")
                .excludePathPatterns("/yuling/security/getPublicKey")
                .excludePathPatterns("/yuling/user/forgotPassword")
                .excludePathPatterns("/yuling/test/**");


    }
}
