package com.simon.microservice.microservicezuul.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
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
}
