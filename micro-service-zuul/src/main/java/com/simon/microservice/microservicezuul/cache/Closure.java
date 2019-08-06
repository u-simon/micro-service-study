package com.simon.microservice.microservicezuul.cache;

/**
 * @author Simon
 * @Date 2019-08-01 15:13
 * @Describe
 */
public interface Closure<O, I> {
	O execute(I input);
}
