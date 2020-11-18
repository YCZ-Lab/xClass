package com.ysh.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpSession;

public class AuthenticationEventListener implements ApplicationListener<AbstractAuthenticationEvent> {

    @Override
    public void onApplicationEvent(AbstractAuthenticationEvent event) {
        System.out.println("==========================EVENT==========================");
        System.out.println("==> " + event.getClass());
        System.out.println("==> " + event.getSource().getClass());
        System.out.println("==> " + event.getAuthentication());
        System.out.println("==> " + event.getAuthentication().getClass());
        System.out.println("==> " + event.getAuthentication().getDetails());
        System.out.println("=========================================================");
    }
}
