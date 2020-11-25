package com.ysh.handler;

import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse resp, AuthenticationException e) throws IOException, ServletException {
        String error;
        if (e instanceof LockedException) {
            error = "Account Locked !!!";
        } else if (e instanceof BadCredentialsException) {
            error = "Bad Credentials !!!";
        } else if (e instanceof DisabledException) {
            error = "Account Disabled !!!";
        } else if (e instanceof AccountExpiredException) {
            error = "Account Expired !!!";
        } else if (e instanceof CredentialsExpiredException) {
            error = "Credentials Expired !!!";
        } else {
            error = e.getMessage();
        }
//        if (req.getRequestURI().indexOf("Mobile") != -1) {
//            resp.sendRedirect("/mobileLogin?error=" + URLEncoder.encode(error, "UTF-8"));
//            return;
//        }
//        resp.sendRedirect("/login?error=" + URLEncoder.encode(error, "UTF-8"));
        String referer = req.getHeader("Referer");
        referer = referer.replaceFirst("https://", "");
        referer = referer.substring(referer.indexOf("/"));
        int i = referer.indexOf("?");
        if (i != -1) {
            referer = referer.substring(0, i);
        }
        super.setDefaultFailureUrl(referer + "?error=" + error);
        super.onAuthenticationFailure(req, resp, e);
    }
}
