package com.simon.microservice.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;

@SpringBootApplication
@EnableEurekaClient
public class MicroServiceConsumerApplication {
	public static void main(String[] args) {
		SpringApplication.run(MicroServiceConsumerApplication.class, args);
	}

}
