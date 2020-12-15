package com.ysh.authentication;

import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;

public class MobilePhoneAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsChecker preAuthenticationChecks = new MobilePhoneAuthenticationProvider.DefaultPreAuthenticationChecks();
    private UserDetailsChecker postAuthenticationChecks = new MobilePhoneAuthenticationProvider.DefaultPostAuthenticationChecks();


    UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String mobilePhone = authentication.getPrincipal() == null ? "NONE_PROVIDED" : authentication.getName();
        UserDetails user = userDetailsService.loadUserByUsername(mobilePhone);
        if (user == null) {
            throw new AuthenticationServiceException("The phone number is not registered");
        }
        preAuthenticationChecks.check(user);
        postAuthenticationChecks.check(user);
        MobilePhoneAuthenticationToken result = new MobilePhoneAuthenticationToken(user, user.getAuthorities());
        result.setDetails(authentication.getDetails());
        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (MobilePhoneAuthenticationToken.class
                .isAssignableFrom(authentication));
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    private class DefaultPreAuthenticationChecks implements UserDetailsChecker {
        @Override
        public void check(UserDetails user) {
            if (!user.isAccountNonLocked()) {
                throw new LockedException("User account is locked !!!");
            }

            if (!user.isEnabled()) {
                throw new DisabledException("User is disabled !!!");
            }

            if (!user.isAccountNonExpired()) {
                throw new AccountExpiredException("User account has expired !!!");
            }
        }
    }

    private class DefaultPostAuthenticationChecks implements UserDetailsChecker {
        @Override
        public void check(UserDetails user) {
            if (!user.isCredentialsNonExpired()) {
                throw new CredentialsExpiredException("User credentials have expired !!!");
            }
        }
    }

}
