package com.ysh.filter;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse resp) throws AuthenticationException {
        if (!req.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + req.getMethod());
        }
        String requestCaptcha = req.getParameter("verifyCode");
        String genCaptcha = (String) req.getSession().getAttribute("verifyCode");
        if (requestCaptcha == null || requestCaptcha.equals("")) {
            throw new AuthenticationServiceException("verification code must be filled!");
        } else if (!genCaptcha.toLowerCase().equals(requestCaptcha.toLowerCase())) {
            throw new AuthenticationServiceException("Verification code error!");
        }
        return super.attemptAuthentication(req, resp);
    }
}
