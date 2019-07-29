package com.simon.microservice.microservicezuul.config;

import com.simon.microservice.microservicezuul.ErrorFilter;
import com.simon.microservice.microservicezuul.IpFilter;
import com.simon.microservice.microservicezuul.LimitFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author fengyue
 * @Date 2019-07-04 11:56
 */
@Configuration
public class AppConfig {

	@Bean
	public IpFilter ipFilter() {
		return new IpFilter();
	}

	@Bean
	public ErrorFilter errorFilter() {
		return new ErrorFilter();
	}

	@Bean
	public LimitFilter limitFilter(){
		return new LimitFilter();
	}
}
