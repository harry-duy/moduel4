package com.moviebooking.system.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* com.moviebooking.system.service.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("Executing method: {} with arguments: {}",
                joinPoint.getSignature().getName(),
                joinPoint.getArgs());
    }

    @After("execution(* com.moviebooking.system.service.*.*(..))")
    public void logAfter(JoinPoint joinPoint) {
        logger.info("Method {} execution completed", joinPoint.getSignature().getName());
    }
}