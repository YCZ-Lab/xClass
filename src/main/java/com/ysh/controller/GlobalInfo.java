package com.ysh.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;
import java.util.Map;

@ControllerAdvice
public class GlobalInfo {
    @ModelAttribute(value = "user")
    public Map<String, String> userInfo(HttpSession session) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken) {
            session.removeAttribute("userName");
        } else {
            session.setAttribute("userName", auth.getName());
        }
        return null;
    }
}
