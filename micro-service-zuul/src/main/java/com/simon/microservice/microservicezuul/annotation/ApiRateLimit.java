package com.simon.microservice.microservicezuul.annotation;

import java.lang.annotation.*;

/**
 * @author Simon
 * @Date 2019-07-29 14:36
 * @Describe 对API进行访问速度限制
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiRateLimit {
    String confKey();
}
