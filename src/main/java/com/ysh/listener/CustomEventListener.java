package com.ysh.listener;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.event.*;
import org.springframework.security.web.session.HttpSessionCreatedEvent;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.ServletRequestHandledEvent;

public class CustomEventListener implements ApplicationListener<ApplicationEvent> {

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ServletRequestHandledEvent) {
            return;
        }
        System.out.println("==========================EVENT==========================");
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            System.out.println("==> " + requestAttributes.getRequest().getRequestURI());
        } else {
            System.out.println("==> requestAttributes is Null");
        }
        System.out.println("==> " + event.getClass());
        System.out.println("==> " + event.getSource().getClass());
        if (event instanceof HttpSessionCreatedEvent) {
            System.out.println("==> CreatedSession: " + ((HttpSessionCreatedEvent) event).getSession().getId());
        } else if (event instanceof HttpSessionDestroyedEvent) {
            System.out.println("==> DestroyedSession: " + ((HttpSessionDestroyedEvent) event).getSession().getId());
        } else if (event instanceof AbstractAuthenticationEvent) {
            System.out.println("==> " + ((AbstractAuthenticationEvent) event).getAuthentication());
            System.out.println("==> " + ((AbstractAuthenticationEvent) event).getAuthentication().getClass());
            System.out.println("==> " + ((AbstractAuthenticationEvent) event).getAuthentication().getDetails());
            if (event instanceof InteractiveAuthenticationSuccessEvent || event instanceof AuthenticationSuccessEvent) {
                // rememberMe登陆时也会触发事件
                requestAttributes.getRequest().getSession().setAttribute("userName", ((AbstractAuthenticationToken) event.getSource()).getName());
            } else if (event instanceof AbstractAuthenticationFailureEvent || event instanceof LogoutSuccessEvent) {
                requestAttributes.getRequest().getSession().removeAttribute("userName");
            }
        }
        System.out.println("=========================================================");
    }
}
