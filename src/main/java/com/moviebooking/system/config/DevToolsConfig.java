package com.moviebooking.system.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class DevToolsConfig {

    @Bean
    @ConditionalOnProperty(name = "app.dev.sql-logging", havingValue = "true")
    public String enableSqlLogging() {
        // Additional SQL logging configuration for development
        System.setProperty("org.jboss.logging.provider", "slf4j");
        return "SQL Logging Enabled";
    }

    @Bean
    @ConditionalOnProperty(name = "app.dev.mock-data", havingValue = "true")
    public String enableMockData() {
        // Configuration for additional mock data in development
        return "Mock Data Enabled";
    }
}