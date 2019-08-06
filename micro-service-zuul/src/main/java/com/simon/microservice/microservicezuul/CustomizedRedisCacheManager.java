package com.simon.microservice.microservicezuul;

import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;

/**
 * @author Simon
 * @Date 2019-07-31 14:04
 * @Describe
 */
public class CustomizedRedisCacheManager extends RedisCacheManager {

    public CustomizedRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
        super(cacheWriter, defaultCacheConfiguration);
    }


    @Override
    public Cache getCache(String name) {
        String[] cacheParams = name.split("##");
        String cacheName = cacheParams[0];
        return super.getCache(cacheName);
    }
}
