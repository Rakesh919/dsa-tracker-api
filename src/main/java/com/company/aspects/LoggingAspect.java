package com.company.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    // Pointcut for service methods
    @Pointcut("execution(* com.company.service..*(..))")
    public void serviceMethods() {}

    // Pointcut for controller methods
    @Pointcut("execution(* com.company.controllers..*(..))")
    public void controllerMethods() {}

    // Combined pointcut for both service and controller methods
    @Pointcut("serviceMethods() || controllerMethods()")
    public void serviceAndControllerMethods() {}

    @Before("serviceAndControllerMethods()")
    public void logMethodStart(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        // Format method name to be more readable (convert camelCase to words)
        String readableMethodName = formatMethodName(methodName);
        String layerType = getLayerType(joinPoint);

        logger.info("{} {} method start", readableMethodName, layerType);
    }

    @AfterThrowing(pointcut = "serviceAndControllerMethods()", throwing = "exception")
    public void logMethodException(JoinPoint joinPoint, Exception exception) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        // Format method name to be more readable
        String readableMethodName = formatMethodName(methodName);
        String layerType = getLayerType(joinPoint);

        logger.error("Exception occurred at {} {} method in {} class - {}",
                readableMethodName, layerType, className, exception.getMessage(), exception);
    }

    /**
     * Converts camelCase method names to readable format
     * e.g., "addProblem" -> "Add Problem"
     */
    private String formatMethodName(String methodName) {
        return methodName.replaceAll("([a-z])([A-Z])", "$1 $2")
                .substring(0, 1).toUpperCase() +
                methodName.replaceAll("([a-z])([A-Z])", "$1 $2").substring(1);
    }

    /**
     * Determines if the method is from the service or controller layer
     */
    private String getLayerType(JoinPoint joinPoint) {
        String packageName = joinPoint.getTarget().getClass().getPackage().getName();
        if (packageName.contains("service")) {
            return "service";
        } else if (packageName.contains("controller")) {
            return "controller";
        }
        return "method"; // fallback
    }
}