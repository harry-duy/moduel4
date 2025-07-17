package com.moviebooking.system.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SecurityAspect {

    private static final Logger logger = LoggerFactory.getLogger(SecurityAspect.class);

    @Before("execution(* com.moviebooking.system.controller.AdminController.*(..))")
    public void logAdminAccess() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Admin access attempted by user: {}", username);
    }

    @Before("execution(* com.moviebooking.system.service.BookingService.createBooking(..))")
    public void logBookingCreation() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Booking creation attempted by user: {}", username);
    }
}