package com.ysh.configuration;

import org.springframework.boot.actuate.audit.InMemoryAuditEventRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class beans {

    @Bean
    public InMemoryHttpTraceRepository getHttpTraceRepository() {
        return new InMemoryHttpTraceRepository();
    }

    @Bean
    public InMemoryAuditEventRepository getAuditEventRepository() {
        return new InMemoryAuditEventRepository();
    }
}
