package com.simon.microservice.microserviceadmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import de.codecentric.boot.admin.server.config.EnableAdminServer;

@SpringBootApplication
@EnableAdminServer
@EnableEurekaClient
public class MicroServiceAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroServiceAdminApplication.class, args);
	}

}