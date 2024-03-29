package com.ysh.configuration;

import com.ysh.handler.CustomAuthenticationFailureHandler;
import com.ysh.handler.CustomAuthenticationSuccessHandler;
import com.ysh.provider.CustomAuthenticationProvider;
import com.ysh.service.CustomRememberMeTokenRepositoryImpl;
import com.ysh.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.net.URLEncoder;
import java.util.Collections;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${server.port}")
    private int httpsPort;
    @Value("${http.port}")
    private int httpPort;

    final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    final CustomRememberMeTokenRepositoryImpl customRememberMeTokenRepositoryImpl;
    final MobilePhoneAuthenticationConfig mobilePhoneAuthenticationConfig;

    public WebSecurityConfig(CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler, CustomAuthenticationFailureHandler customAuthenticationFailureHandler, CustomRememberMeTokenRepositoryImpl customRememberMeTokenRepositoryImpl, MobilePhoneAuthenticationConfig mobilePhoneAuthenticationConfig) {
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
        this.customAuthenticationFailureHandler = customAuthenticationFailureHandler;
        this.customRememberMeTokenRepositoryImpl = customRememberMeTokenRepositoryImpl;
        this.mobilePhoneAuthenticationConfig = mobilePhoneAuthenticationConfig;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/css/**")
                .antMatchers("/icon-fonts/**")
                .antMatchers("/img/**")
                .antMatchers("/js/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.portMapper().http(httpPort).mapsTo(httpsPort);
        http.requiresChannel(channel -> channel.anyRequest().requiresSecure());

//        http.addFilterBefore(new MobilePhoneVerifyCodeFilter(customAuthenticationFailureHandler), UsernamePasswordAuthenticationFilter.class);

        http.authorizeRequests()
                .antMatchers("/admin/**")
                .hasRole("admin")
                .antMatchers("/submit/**")
                .rememberMe()
                .antMatchers("/submit/**")
                .hasRole("user")
//                .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("admin")
//                .antMatchers("/monitor/**")
//                .fullyAuthenticated()
                .antMatchers("/monitor/**")
                .hasRole("admin")
//                .antMatchers("/monitor/**").fullyAuthenticated()
                .anyRequest()
                .permitAll();

        http.formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/doLogin")
                .usernameParameter("name")
                .passwordParameter("password")
//                .successHandler((req, resp, auth) -> {
//                    String referer = req.getHeader("referer");
//                    if (referer == null || referer.contains("login")) {
//                        resp.sendRedirect("/index");
//                    } else {
//                        resp.sendRedirect(referer);
//                    }
//                })
                .successHandler(customAuthenticationSuccessHandler)
//                .failureHandler((req, resp, e) -> {
//                    String error;
//                    if (e instanceof LockedException) {
//                        error = "Account Locked !!!";
//                    } else if (e instanceof BadCredentialsException) {
//                        error = "Bad Credentials !!!";
//                    } else if (e instanceof DisabledException) {
//                        error = "Account Disabled !!!";
//                    } else if (e instanceof AccountExpiredException) {
//                        error = "Account Expired !!!";
//                    } else if (e instanceof CredentialsExpiredException) {
//                        error = "Credentials Expired !!!";
//                    } else {
//                        error = e.getMessage();
//                    }
//                    resp.sendRedirect("/login?error=" + URLEncoder.encode(error, "UTF-8"));
//                })
                .failureHandler(customAuthenticationFailureHandler)
                .and()
                .rememberMe()
                .rememberMeCookieName("remember")
                .rememberMeParameter("remember")
                .key("xClass")
                .tokenRepository(customRememberMeTokenRepositoryImpl)
                .tokenValiditySeconds(60 * 60 * 24 * 30)
                .useSecureCookie(true);

        http.logout()
                .logoutUrl("/logout")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/index")
                .deleteCookies("JSESSIONID");

        http.csrf().disable();

        http.sessionManagement()
                .invalidSessionUrl("/login?error=" + URLEncoder.encode("The session has expired, please log in again !", "UTF-8"))
                .maximumSessions(1)
                .expiredUrl("/login?error=" + URLEncoder.encode("You have logged in on another device, this login was forced to go offline !", "UTF-8"))
                .maxSessionsPreventsLogin(false);

        http.apply(mobilePhoneAuthenticationConfig);
    }

    CustomAuthenticationProvider customAuthenticationProvider() {
        CustomAuthenticationProvider customAuthenticationProvider = new CustomAuthenticationProvider();
        customAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        customAuthenticationProvider.setUserDetailsService(userDetailsService());
        return customAuthenticationProvider;
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return new UserService();
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() {
        return new ProviderManager(Collections.singletonList(customAuthenticationProvider()));
    }
}
