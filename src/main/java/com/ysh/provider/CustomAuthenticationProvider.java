package com.ysh.provider;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class CustomAuthenticationProvider extends DaoAuthenticationProvider {
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String requestCaptcha = req.getParameter("verifyCode");
        String genCaptcha = (String) req.getSession().getAttribute("verifyCode");
        if (requestCaptcha == null || "".equals(requestCaptcha)) {
            throw new AuthenticationServiceException("verification code must be filled!");
        } else if (genCaptcha == null || "".equals(genCaptcha)) {
            throw new AuthenticationServiceException("verification code lost!");
        }
        req.getSession().removeAttribute("verifyCode");
        if (!genCaptcha.toLowerCase().equals(requestCaptcha.toLowerCase())) {
            throw new AuthenticationServiceException("Verification code error!");
        }
        super.additionalAuthenticationChecks(userDetails, authentication);
    }
}
