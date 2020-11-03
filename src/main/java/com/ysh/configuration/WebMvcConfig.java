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
        registry.addViewController("login").setViewName("login");
        registry.addViewController("index").setViewName("index");
        registry.addViewController("jobs").setViewName("jobs");
        registry.addViewController("fPodcasts").setViewName("fPodcasts");
        registry.addViewController("fArticles").setViewName("fArticles");
        registry.addViewController("fVideos").setViewName("fVideos");
        registry.addViewController("finances").setViewName("fPodcasts");
        registry.addViewController("ePodcasts").setViewName("ePodcasts");
        registry.addViewController("eArticles").setViewName("eArticles");
        registry.addViewController("eVideos").setViewName("eVideos");
        registry.addViewController("entrepreneurship").setViewName("ePodcasts");
        registry.addViewController("contact").setViewName("contact");
        registry.addViewController("ysh").setViewName("ysh");
        registry.addViewController("testIndex").setViewName("testIndex");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new Request(logsService)).excludePathPatterns("/lib/**");
//        registry.addInterceptor(new VerifyCodeRegister()).addPathPatterns("/register");
    }
}
