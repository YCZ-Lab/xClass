package com.ysh.configuration;

import com.ysh.filter.VerifyCode;
import com.ysh.service.RememberMeTokenRepositoryService;
import com.ysh.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.util.ReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${server.port}")
    private int httpsPort;
    @Value("${http.port}")
    private int httpPort;

    final UserService userService;
    final VerifyCode verifyCode;

    final RememberMeTokenRepositoryService rememberMeTokenRepositoryService;

    public WebSecurityConfig(UserService userService, VerifyCode verifyCode, RememberMeTokenRepositoryService rememberMeTokenRepositoryService) {
        this.userService = userService;
        this.verifyCode = verifyCode;
        this.rememberMeTokenRepositoryService = rememberMeTokenRepositoryService;
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
        http.rememberMe().key("xClass").tokenRepository(rememberMeTokenRepositoryService)
                .addObjectPostProcessor(new ObjectPostProcessor<RememberMeAuthenticationFilter>() {
                    @Override
                    public <O extends RememberMeAuthenticationFilter> O postProcess(O object) {

                        RememberMeAuthenticationFilter newFilter = new RememberMeAuthenticationFilter(
                                (AuthenticationManager) getByReflection(object, "authenticationManager"),
                                (RememberMeServices) getByReflection(object, "rememberMeServices")
                        ) {
                            @Override
                            protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) {
                                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                                request.getSession().setAttribute("userName", auth.getName());
                            }
                        };
                        return (O) newFilter;
                    }

                    private <O extends RememberMeAuthenticationFilter> Object getByReflection(O object, String name) {
                        Field field = ReflectionUtils.findField(object.getClass(), name);
                        ReflectionUtils.makeAccessible(field);
                        return ReflectionUtils.getField(field, object);
                    }
                });
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
                .loginProcessingUrl("/login")
                .usernameParameter("name")
                .passwordParameter("password")
                .successHandler((req, resp, auth) -> {
                    req.getSession().setAttribute("userName", auth.getName());
                    String referer = req.getHeader("referer");
                    if (referer.contains("login")) {
                        resp.sendRedirect("/index");
                    } else {
                        resp.sendRedirect(referer);
                    }
                })
                .failureHandler((req, resp, e) -> {
                    System.out.println("==> " + e);
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
//                .rememberMe()
//                .key("xClass")
//                .and()
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
