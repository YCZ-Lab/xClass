package com.ysh.configuration;

import com.ysh.filter.VerifyCode;
import com.ysh.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${server.port}")
    private int httpsPort;
    @Value("${http.port}")
    private int httpPort;

    final UserService userService;
    final VerifyCode verifyCode;

    public WebSecurityConfig(UserService userService, VerifyCode verifyCode) {
        this.userService = userService;
        this.verifyCode = verifyCode;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("YCZ").password("770519").roles("admin")
//                .and()
//                .withUser("YSH").password("050406").roles("user");
        auth.userDetailsService(userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.portMapper().http(httpPort).mapsTo(httpsPort);
        http.requiresChannel(channel -> channel.anyRequest().requiresSecure());
        http.addFilterBefore(verifyCode, UsernamePasswordAuthenticationFilter.class);
        http.authorizeRequests()
                .antMatchers("/admin/**")
                .hasRole("admin")
                .antMatchers("/submit/**")
                .hasRole("user")
                .anyRequest()
                .permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("name")
                .passwordParameter("password")
                .successHandler((req, resp, auth) -> {
                    req.getSession().setAttribute("logined", true);
                    String referer = req.getHeader("referer");
                    if (referer.contains("login")) {
                        resp.sendRedirect("/index");
                    } else {
                        resp.sendRedirect(referer);
                    }
                })
                .failureHandler((req, resp, e) -> {
                    String error = "Unknown Error !!!";
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
                    }
                    resp.sendRedirect("/login?error=" + error);
                })
                .and()
                .logout()
                .logoutUrl("/logout")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/index")
                .and()
                .csrf().disable();
    }

    @Bean
    RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = "ROLE_admin > ROLE_user";
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }

}
