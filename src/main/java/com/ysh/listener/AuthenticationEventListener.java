package com.ysh.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;

public class AuthenticationEventListener implements ApplicationListener<AbstractAuthenticationEvent> {
    @Override
    public void onApplicationEvent(AbstractAuthenticationEvent event) {
        System.out.println("==========================EVENT==========================");
        System.out.println("==> " + event.getClass());
        System.out.println("==> " + event.getSource().getClass());
        System.out.println("==> " + event.getAuthentication().getClass());
        System.out.println("==> " + event.getAuthentication().getDetails());
        System.out.println("=========================================================");
    }
}
