package com.simon.microservice.consumer.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author fengyue
 * @Date 2019-06-19 15:00
 */
@Configuration
public class BeanConfig {

	@Bean
	@LoadBalanced
	// @MyLoadBalancer
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
}
