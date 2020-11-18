package com.ysh.configuration;

import com.ysh.filter.CustomUsernamePasswordAuthenticationFilter;
import com.ysh.service.CustomRememberMeTokenRepositoryImpl;
import com.ysh.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    final RememberMeServices persistentTokenBasedRememberMeServices;
    final CustomRememberMeTokenRepositoryImpl customRememberMeTokenRepositoryImpl;

    public WebSecurityConfig(UserService userService, RememberMeServices rememberMeServices, CustomRememberMeTokenRepositoryImpl customRememberMeTokenRepositoryImpl) {
        this.userService = userService;
        this.persistentTokenBasedRememberMeServices = rememberMeServices;
        this.customRememberMeTokenRepositoryImpl = customRememberMeTokenRepositoryImpl;

    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.portMapper().http(httpPort).mapsTo(httpsPort);
        http.requiresChannel(channel -> channel.anyRequest().requiresSecure());
        http.addFilterAt(customUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//        http.addFilterAt(new RememberMeAuthenticationFilter(authenticationManagerBean(), rememberMeServices), RememberMeAuthenticationFilter.class);
        //以下代码会注册一个匿名的RememberMeAuthenticationFilter
//        http.rememberMe()
//                .key("xClass")
//                .tokenRepository(customRememberMeTokenRepositoryImpl)
//                .addObjectPostProcessor(new ObjectPostProcessor<RememberMeAuthenticationFilter>() {
//                    @Override
//                    public <O extends RememberMeAuthenticationFilter> O postProcess(O object) {
//
//                        RememberMeAuthenticationFilter newFilter = new RememberMeAuthenticationFilter(
//                                (AuthenticationManager) getByReflection(object, "authenticationManager"),
//                                (RememberMeServices) getByReflection(object, "rememberMeServices")
//                        ) {
//                            @Override
//                            protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) {
//                                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//                                request.getSession().setAttribute("userName", auth.getName());
//                            }
//                        };
//                        return (O) newFilter;
//                    }
//
//                    private <O extends RememberMeAuthenticationFilter> Object getByReflection(O object, String name) {
//                        Field field = ReflectionUtils.findField(object.getClass(), name);
//                        ReflectionUtils.makeAccessible(field);
//                        return ReflectionUtils.getField(field, object);
//                    }
//                });
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
//                .formLogin()
//                .loginPage("/loginPage")
//                .loginProcessingUrl("/login")
//                .usernameParameter("name")
//                .passwordParameter("password")
//                .successHandler((req, resp, auth) -> {
//                    req.getSession().setAttribute("userName", auth.getName());
//                    String referer = req.getHeader("referer");
//                    if (referer.contains("login")) {
//                        resp.sendRedirect("/index");
//                    } else {
//                        resp.sendRedirect(referer);
//                    }
//                })
//                .failureHandler((req, resp, e) -> {
//                    System.out.println("==> " + e);
//                    String error = "Unknown Error !!!";
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
//                    }
//                    resp.sendRedirect("/loginPage?error=" + error);
//                })
//                .and()
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
    }

    //    @Bean //不要打开@Bean注解,否则会被当作普通Filter, 自动加载到ApplicationFilterChain中
    // 通过HttpSecurity的addFilter方法,手工加入到SpringSecurity中的DefaultSecurityFilterChain里
    CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter() throws Exception {
        CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter = new CustomUsernamePasswordAuthenticationFilter();
        customUsernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler((req, resp, auth) -> {
//            req.getSession().setAttribute("userName", auth.getName());
            String referer = req.getHeader("referer");
            if (referer.contains("loginPage")) {
                resp.sendRedirect("/index");
            } else {
                resp.sendRedirect(referer);
            }
        });
        customUsernamePasswordAuthenticationFilter.setAuthenticationFailureHandler((req, resp, e) -> {
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
            resp.sendRedirect("/loginPage?error=" + error);
        });
        customUsernamePasswordAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
        customUsernamePasswordAuthenticationFilter.setRememberMeServices(persistentTokenBasedRememberMeServices);
        customUsernamePasswordAuthenticationFilter.setFilterProcessesUrl("/login");
        customUsernamePasswordAuthenticationFilter.setUsernameParameter("name");
        customUsernamePasswordAuthenticationFilter.setPasswordParameter("password");
        return customUsernamePasswordAuthenticationFilter;
    }
}
