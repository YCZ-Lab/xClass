package com.ysh.configuration;

import com.ysh.service.CustomRememberMeTokenRepositoryImpl;
import com.ysh.service.UserService;
import org.springframework.boot.actuate.audit.InMemoryAuditEventRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.stereotype.Component;

@Component
public class beans {

    final UserService userService;
    final CustomRememberMeTokenRepositoryImpl customRememberMeTokenRepositoryImpl;

    public beans(UserService userService, CustomRememberMeTokenRepositoryImpl customRememberMeTokenRepositoryImpl) {
        this.customRememberMeTokenRepositoryImpl = customRememberMeTokenRepositoryImpl;
        this.userService = userService;
    }

    @Bean
    RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = "ROLE_admin > ROLE_user";
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }

    @Bean
    public InMemoryHttpTraceRepository getHttpTraceRepository() {
        return new InMemoryHttpTraceRepository();
    }

    @Bean
    public InMemoryAuditEventRepository getAuditEventRepository() {
        return new InMemoryAuditEventRepository();
    }

    @Bean
    RememberMeServices getPersistentTokenBasedRememberMeServices() {
        return new PersistentTokenBasedRememberMeServices("xClass", userService, customRememberMeTokenRepositoryImpl);
    }

}
