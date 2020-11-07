package com.ysh.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class VerifyCode extends GenericFilterBean {
    private final String defaultFilterProcessUrl = "/login /register";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        if ("POST".equalsIgnoreCase(request.getMethod()) && defaultFilterProcessUrl.contains(request.getServletPath())) {
            // 验证码验证
            String requestCaptcha = request.getParameter("verifyCode");
            String genCaptcha = (String) request.getSession().getAttribute("verifyCode");
            String uri = request.getRequestURI();
            if (requestCaptcha == null || requestCaptcha.equals("")) {
//                throw new AuthenticationServiceException("验证码不能为空!");
                if (uri.equals("/login")) {
                    response.sendRedirect("/login?error=verification code must be filled!");
                } else if (uri.equals("/register")) {
                    response.sendRedirect("/index?error=verification code must be filled!");
                } else {
                    response.sendRedirect("/index?error=unknown Error!");
                }
            } else if (!genCaptcha.toLowerCase().equals(requestCaptcha.toLowerCase())) {
//                throw new AuthenticationServiceException("验证码错误!");
                if (uri.equals("/login")) {
                    response.sendRedirect("/login?error=Verification code error!");
                } else if (uri.equals("/register")) {
                    response.sendRedirect("/index?error=Verification code error!");
                } else {
                    response.sendRedirect("/index?error=unknown Error!");
                }
            } else {
                chain.doFilter(request, response);
            }
        } else {
            chain.doFilter(request, response);
        }
    }
}