package com.simon.microservce.hystrix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

@EnableCircuitBreaker
@SpringBootApplication
public class MicroServiceHystrixApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroServiceHystrixApplication.class, args);
	}

}
