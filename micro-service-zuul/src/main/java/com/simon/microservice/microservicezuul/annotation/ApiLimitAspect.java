package com.simon.microservice.microservicezuul.annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

/**
 * @author Simon
 * @Date 2019-07-29 14:48
 * @Describe
 */
@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApiLimitAspect {

    public static Map<String, Semaphore> semaphoreMap  = new ConcurrentHashMap<>();

    @Pointcut("execution(* *.*(..))")
    public void pointcut(){}

    @Around("pointcut()")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint){
        Semaphore semaphore = null;
        Class<?> aClass = joinPoint.getTarget().getClass();
        String key = getRateLimitKey(aClass, joinPoint.getSignature().getName());
        if (key != null){
            semaphore = semaphoreMap.get(key);
        } else {
            semaphore = semaphoreMap.get("open.api.defaultLimit");
        }
        try {
            semaphore.acquire();
            return joinPoint.proceed();
        }  catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            semaphore.release();
        }

        return null;
    }

    private String getRateLimitKey(Class clazz, String methodName){
        for (Method method : clazz.getDeclaredMethods()){
            if (method.getName().equals(methodName)){
                if (method.isAnnotationPresent(ApiRateLimit.class)){
                    return method.getAnnotation(ApiRateLimit.class).confKey();
                }
            }
        }
        return null;
    }
}
