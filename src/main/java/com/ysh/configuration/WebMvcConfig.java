package com.ysh.configuration;

import com.ysh.interceptor.Request;
import com.ysh.service.LogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    LogsService logsService;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("test").setViewName("test");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new Request(logsService))
                .excludePathPatterns("/css/**")
                .excludePathPatterns("/icon-fonts/**")
                .excludePathPatterns("/img/**")
                .excludePathPatterns("/js/**");
//        registry.addInterceptor(new VerifyCodeRegister()).addPathPatterns("/register");
    }
}
