package com.epam.esm.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * aspect for logging method calls
 * and exceptions thrown by methods in Spring components annotated with
 * {@link org.springframework.stereotype.Repository @Repository},
 * {@link org.springframework.stereotype.Service @Service},
 * {@link org.springframework.web.bind.annotation.RestController @RestController},
 * {@link org.springframework.web.bind.annotation.ControllerAdvice @ControllerAdvice}
 *
 * @author bakhridinova
 */

@Aspect
@Component
public class CustomLoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(CustomLoggingAspect.class);

    @Pointcut("within(@org.springframework.stereotype.Repository *)" +
            " || within(@org.springframework.stereotype.Service *)" +
            " || within(@org.springframework.web.bind.annotation.RestController *)" +
            " || within(@org.springframework.web.bind.annotation.ControllerAdvice *)")
    public void loggingTargets() {
    }

    @Before("loggingTargets())")
    public void logBeforeExecution(JoinPoint joinPoint) {
        logger.info("executing method: " + joinPoint.getSignature().getDeclaringTypeName() + "."
                + joinPoint.getSignature().getName() + " with arguments " + Arrays.asList(joinPoint.getArgs()));
    }

    @Around("loggingTargets()")
    public Object logDuringExecution(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long start = System.nanoTime();
        Object proceed = proceedingJoinPoint.proceed();
        long end = System.nanoTime();
        logger.info("execution of " + proceedingJoinPoint.getSignature().getDeclaringTypeName() + "."
                + proceedingJoinPoint.getSignature().getName() + " took " +
                TimeUnit.NANOSECONDS.toMillis(end - start) + " milli seconds");
        return proceed;
    }

    @AfterReturning(value = "loggingTargets()", returning = "value")
    public void logAfterExecution(JoinPoint joinPoint, Object value) {
        logger.info("method " + joinPoint.getSignature().getDeclaringTypeName() + "."
                + joinPoint.getSignature().getName() + " executed and return value [" + value + "]");
    }

    @AfterThrowing(value = "loggingTargets()", throwing = "exception")
    public void logAfterException(JoinPoint joinPoint, Exception exception) {
        logger.debug("method " + joinPoint.getSignature().getDeclaringTypeName() + "."
                + joinPoint.getSignature().getName() + " threw " + exception);
    }
}
