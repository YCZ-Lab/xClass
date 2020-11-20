package com.ysh.configuration;

import com.ysh.provider.CustomAuthenticationProvider;
import com.ysh.service.CustomRememberMeTokenRepositoryImpl;
import com.ysh.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${server.port}")
    private int httpsPort;
    @Value("${http.port}")
    private int httpPort;

    final CustomRememberMeTokenRepositoryImpl customRememberMeTokenRepositoryImpl;

    public WebSecurityConfig(CustomRememberMeTokenRepositoryImpl customRememberMeTokenRepositoryImpl) {
        this.customRememberMeTokenRepositoryImpl = customRememberMeTokenRepositoryImpl;

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
        http.authenticationProvider(customAuthenticationProvider());
        http.authorizeRequests()
                .antMatchers("/admin/**")
                .hasRole("admin")
                .antMatchers("/submit/**")
                .hasRole("user")
//                .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("admin")
                .antMatchers("/monitor/**")
                .hasRole("admin")
//                .antMatchers("/monitor/**").fullyAuthenticated()
                .anyRequest()
                .permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/doLogin")
                .usernameParameter("name")
                .passwordParameter("password")
                .successHandler((req, resp, auth) -> {
                    String referer = req.getHeader("referer");
                    if (referer.contains("login")) {
                        resp.sendRedirect("/index");
                    } else {
                        resp.sendRedirect(referer);
                    }
                })
                .failureHandler((req, resp, e) -> {
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
                    resp.sendRedirect("/login?error=" + error);
                })
                .and()
                .rememberMe()
                .key("xClass")
                .tokenRepository(customRememberMeTokenRepositoryImpl)
                .and()
                .logout()
                .logoutUrl("/logout")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/index")
                .and()
                .csrf().disable();
//                .sessionManagement()
//                .maximumSessions(1);
    }

    @Bean
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

//    @Bean
//    @Override
//    protected AuthenticationManager authenticationManager() throws Exception {
//        ProviderManager providerManager = new ProviderManager(Arrays.asList(customAuthenticationProvider()));
//        return providerManager;
//    }
}
