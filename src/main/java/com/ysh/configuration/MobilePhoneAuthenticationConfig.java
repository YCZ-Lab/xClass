package com.ysh.configuration;

import com.ysh.authentication.MobilePhoneAuthenticationProvider;
import com.ysh.filter.MobilePhoneAuthenticationFilter;
import com.ysh.filter.MobilePhoneVerifyCodeFilter;
import com.ysh.handler.CustomAuthenticationFailureHandler;
import com.ysh.handler.CustomAuthenticationSuccessHandler;
import com.ysh.service.MobilePhoneUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.stereotype.Component;

@Component
public class MobilePhoneAuthenticationConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Autowired
    private MobilePhoneUserDetailsService mobilePhoneUserDetailsService;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        MobilePhoneAuthenticationFilter mobilePhoneAuthenticationFilter = new MobilePhoneAuthenticationFilter();
        final AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        mobilePhoneAuthenticationFilter.setAuthenticationManager(authenticationManager);
        mobilePhoneAuthenticationFilter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);
        mobilePhoneAuthenticationFilter.setAuthenticationFailureHandler(customAuthenticationFailureHandler);
        final SessionAuthenticationStrategy sessionAuthenticationStrategy = http.getSharedObject(SessionAuthenticationStrategy.class);
        mobilePhoneAuthenticationFilter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy);
        http.addFilterBefore(new MobilePhoneVerifyCodeFilter(customAuthenticationFailureHandler), UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(mobilePhoneAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        MobilePhoneAuthenticationProvider mobilePhoneAuthenticationProvider = new MobilePhoneAuthenticationProvider();
        mobilePhoneAuthenticationProvider.setUserDetailsService(mobilePhoneUserDetailsService);
        http.authenticationProvider(mobilePhoneAuthenticationProvider);
        final RememberMeServices rememberMeServices = http.getSharedObject(RememberMeServices.class);
        mobilePhoneAuthenticationFilter.setRememberMeServices(rememberMeServices);
    }
}
