package com.simon.microservice.microserviceadmin;

import com.simon.microservice.microserviceadmin.notify.DingDingNotifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableAdminServer
@EnableEurekaClient
public class MicroServiceAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroServiceAdminApplication.class, args);
	}

	@Bean
	public DingDingNotifier dingDingNotifier(){
		return new DingDingNotifier(null);
	}
}