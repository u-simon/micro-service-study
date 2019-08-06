package com.simon.microservice.microservicezuul.cache;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author Simon
 * @Date 2019-08-01 15:14
 * @Describe
 */
public class CacheServiceImpl implements CacheService {

	@Autowired
	StringRedisTemplate stringRedisTemplate;

	private long timeout = 1L;

	private TimeUnit timeUnit = TimeUnit.HOURS;

	@Override
	public void setCache(String key, String value, Long timeout, TimeUnit timeUnit) {
		stringRedisTemplate.opsForValue().set(key, value, timeout, timeUnit);
	}

	@Override
	public String getCache(String key) {
		return stringRedisTemplate.opsForValue().get(key);
	}

	@Override
	public <V, K> String getCahce(K key, Closure<V, K> closure) {
		return doGetCache(key, closure, this.timeout, this.timeUnit);
	}

	@Override
	public <V, K> String getCache(K key, Closure<V, K> closure, long timeout, TimeUnit timeUnit) {
		return doGetCache(key, closure, timeout, timeUnit);
	}

	@Override
	public void deleteCache(String key) {
		stringRedisTemplate.delete(key);
	}

	private <V, K> String doGetCache(K key, Closure<V, K> closure, long timeout,
			TimeUnit timeUnit) {
		String ret = getCache(key.toString());
		if (ret == null) {
			Object execute = closure.execute(key);
			setCache(key.toString(), execute.toString(), timeout, timeUnit);
			return execute.toString();
		}
		return ret;
	}
}
