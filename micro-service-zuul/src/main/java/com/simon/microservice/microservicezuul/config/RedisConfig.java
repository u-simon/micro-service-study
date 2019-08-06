package com.simon.microservice.microservicezuul.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author Simon
 * @Date 2019-07-29 11:15
 */
@Configuration
public class RedisConfig {

	@Autowired
	JedisConnectionFactory jedisConnectionFactory;

	@Bean(name = "longRedisTemplate")
	public RedisTemplate<String, Long> redisTemplate() {
		RedisTemplate<String, Long> template = new RedisTemplate<>();
		template.setConnectionFactory(jedisConnectionFactory);
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashValueSerializer(new GenericToStringSerializer<Long>(Long.class));
		template.setValueSerializer(new GenericToStringSerializer<Long>(Long.class));
		return template;
	}

	@Bean
	public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
		RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(factory);
		redisTemplate.afterPropertiesSet();
		setSerializer(redisTemplate);
		return redisTemplate;
	}

	private void setSerializer(RedisTemplate<String, String> redisTemplate) {
		Jackson2JsonRedisSerializer jackson2JsonRedisSerializer =
				new Jackson2JsonRedisSerializer(Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(om);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
	}

	@Bean
	public CacheErrorHandler errorHandler() {
		/**
		 * redis 数据操作异常处理，这里的处理：在日志打印出错信息，但是放行保证Redis服务器出现连接等问题的时候不影响程序的正常运行
		 * 使得能够出问题时不用缓存，继续执行业务逻辑去查询DB
		 */
		return new CacheErrorHandler() {
			@Override
			public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {

			}

			@Override
			public void handleCachePutError(RuntimeException exception, Cache cache, Object key,
					Object value) {

			}

			@Override
			public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {

			}

			@Override
			public void handleCacheClearError(RuntimeException exception, Cache cache) {

			}
		};
	}
}
