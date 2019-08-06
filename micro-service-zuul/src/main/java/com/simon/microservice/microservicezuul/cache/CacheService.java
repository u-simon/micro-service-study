package com.simon.microservice.microservicezuul.cache;

import java.util.concurrent.TimeUnit;

/**
 * @author Simon
 * @Date 2019-08-01 15:10
 * @Describe
 */
public interface CacheService {

    public void setCache(String key, String value, Long timeout, TimeUnit timeUnit);

    public String getCache(String key);

    public <V, K> String getCahce(K key, Closure<V, K> closure);

    <V, K> String getCache(K key, Closure<V, K> closure, long timeout, TimeUnit timeUnit);

    void deleteCache(String key);
}
