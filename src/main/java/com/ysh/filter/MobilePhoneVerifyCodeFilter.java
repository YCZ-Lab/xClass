package com.ysh.filter;

import com.ysh.handler.CustomAuthenticationFailureHandler;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@Component
public class MobilePhoneVerifyCodeFilter extends OncePerRequestFilter {

    final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    public MobilePhoneVerifyCodeFilter(CustomAuthenticationFailureHandler customAuthenticationFailureHandler) {
        this.customAuthenticationFailureHandler = customAuthenticationFailureHandler;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if ("/doMobileLogin".equals(request.getRequestURI()) && "POST".equals(request.getMethod())) {
            String requestCaptcha = request.getParameter("verifyCode");
            String genCaptcha = (String) request.getSession().getAttribute("mobilePhoneCode");
            if (requestCaptcha == null || "".equals(requestCaptcha)) {
                customAuthenticationFailureHandler.onAuthenticationFailure(request, response, new AuthenticationServiceException("mobile phone verification code must be filled!"));
                return;
            } else if (genCaptcha == null || "".equals(genCaptcha)) {
                customAuthenticationFailureHandler.onAuthenticationFailure(request, response, new AuthenticationServiceException("mobile phone verification code lost!"));
                return;
            }
            request.getSession().removeAttribute("mobilePhoneCode");
            if (!genCaptcha.toLowerCase().equals(requestCaptcha.toLowerCase())) {
                customAuthenticationFailureHandler.onAuthenticationFailure(request, response, new AuthenticationServiceException("mobile phone verification code error!"));
                return;
            }
        }
        filterChain.doFilter(request, response);
    }


}
