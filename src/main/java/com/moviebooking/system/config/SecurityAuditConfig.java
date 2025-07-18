package com.moviebooking.system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.context.event.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class SecurityAuditConfig {

    private static final Logger auditLogger = LoggerFactory.getLogger("AUDIT");
    private static final Logger securityLogger = LoggerFactory.getLogger("SECURITY");

    @EventListener
    public void auditEventHappened(AuthenticationSuccessEvent success) {
        String username = success.getAuthentication().getName();
        String authorities = success.getAuthentication().getAuthorities().toString();
        auditLogger.info("Login successful for user: {} with authorities: {}", username, authorities);
    }

    @EventListener
    public void auditEventHappened(AuthenticationFailureBadCredentialsEvent failure) {
        String username = failure.getAuthentication().getName();
        securityLogger.warn("Login failed for user: {} - Bad credentials", username);
    }
}
