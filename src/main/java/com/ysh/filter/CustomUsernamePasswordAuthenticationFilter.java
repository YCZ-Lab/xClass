package com.ysh.filter;

import com.ysh.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@Component
public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

//    SessionRegistry sessionRegistry;

    //    public CustomUsernamePasswordAuthenticationFilter(SessionRegistry sessionRegistry) {
//        this.sessionRegistry = sessionRegistry;
//    }
    public CustomUsernamePasswordAuthenticationFilter() {
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse resp) throws AuthenticationException {
        if (!req.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + req.getMethod());
        }
        String requestCaptcha = req.getParameter("verifyCode");
        String genCaptcha = (String) req.getSession().getAttribute("verifyCode");
        if (requestCaptcha == null || requestCaptcha.equals("")) {
            throw new AuthenticationServiceException("verification code must be filled!");
        } else if (genCaptcha == null || genCaptcha.equals("")) {
            throw new AuthenticationServiceException("verification code lost!");
        } else if (!genCaptcha.toLowerCase().equals(requestCaptcha.toLowerCase())) {
            throw new AuthenticationServiceException("Verification code error!");
        }

        String username = obtainUsername(req);
        String password = obtainPassword(req);

        if (username == null) {
            username = "";
        }

        if (password == null) {
            password = "";
        }

        username = username.trim();

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                username, password);

        // Allow subclasses to set the "details" property
        setDetails(req, authRequest);
//        User user = new User();
//        user.setUserName(username);
//        if (sessionRegistry != null) {
//            sessionRegistry.registerNewSession(req.getSession(true).getId(), user);
//        } else {
//            System.out.println("sessionRegistry is null");
//        }
        return this.getAuthenticationManager().authenticate(authRequest);

//        return super.attemptAuthentication(req, resp);
    }


}
