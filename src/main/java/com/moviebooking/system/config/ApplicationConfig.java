// ApplicationConfig.java - Simplified
package com.moviebooking.system.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableAsync
@EnableTransactionManagement
public class ApplicationConfig {

    @Value("${app.name:CinemaMax}")
    private String appName;

    @Value("${app.version:1.0.0}")
    private String appVersion;

    @Bean
    public String appName() {
        return appName;
    }

    @Bean
    public String appVersion() {
        return appVersion;
    }
}