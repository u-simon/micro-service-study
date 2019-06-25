package com.simon.microservice.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MicroServerProviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroServerProviderApplication.class, args);
	}

}
