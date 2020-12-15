package com.ysh.filter;

import com.ysh.authentication.MobilePhoneAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MobilePhoneAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public MobilePhoneAuthenticationFilter() {
        super(new AntPathRequestMatcher("/doMobileLogin", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!"POST".equals(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not support: " + request.getMethod());
        }
        String mobilePhone = request.getParameter("mobilePhoneNumber");
        if (mobilePhone == null) {
            mobilePhone = "";
        }
        mobilePhone = mobilePhone.trim();
        MobilePhoneAuthenticationToken authRequest = new MobilePhoneAuthenticationToken(mobilePhone);
        this.setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    protected void setDetails(HttpServletRequest request, MobilePhoneAuthenticationToken authRequest)
    {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }
}
