// CacheConfig.java - Simplified
package com.moviebooking.system.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
        // Khởi tạo cache names một cách đơn giản
        cacheManager.setCacheNames(java.util.Arrays.asList(
                "movies",
                "theaters",
                "showtimes",
                "users"
        ));
        return cacheManager;
    }
}