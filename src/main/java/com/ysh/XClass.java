package com.ysh;

import com.ysh.listener.CustomEventListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class XClass {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(XClass.class);
        springApplication.addListeners(new CustomEventListener());
        springApplication.run(args);
//        SpringApplication.run(XClass.class, args);
    }

}
