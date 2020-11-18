package com.ysh.configuration;

import com.ysh.interceptor.Request;
import com.ysh.service.LogsService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    final LogsService logsService;

    public WebMvcConfig(LogsService logsService) {
        this.logsService = logsService;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("loginPage").setViewName("login");
        registry.addViewController("index").setViewName("index");
        registry.addViewController("jobs").setViewName("jobs");
        registry.addViewController("finances").setViewName("fPodcasts");
        registry.addViewController("fArticles").setViewName("fArticles");
        registry.addViewController("fVideos").setViewName("fVideos");
        registry.addViewController("fPodcasts").setViewName("fPodcasts");
        registry.addViewController("entrepreneurship").setViewName("ePodcasts");
        registry.addViewController("eArticles").setViewName("eArticles");
        registry.addViewController("eVideos").setViewName("eVideos");
        registry.addViewController("ePodcasts").setViewName("ePodcasts");
        registry.addViewController("contact").setViewName("contact");
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
