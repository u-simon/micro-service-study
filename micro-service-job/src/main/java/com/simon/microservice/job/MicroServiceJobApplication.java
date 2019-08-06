package com.simon.microservice.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ImportResource;

import java.util.concurrent.CountDownLatch;

@SpringBootApplication
@ImportResource(locations = {"classpath:applicationContext.xml"})
public class MicroServiceJobApplication {

	public static void main(String[] args) {
//		SpringApplication.run(MicroServiceJobApplication.class, args);
		new SpringApplicationBuilder().sources(MicroServiceJobApplication.class).web(WebApplicationType.NONE).run(args);

		try {
			new CountDownLatch(1).await();
		} catch (InterruptedException e) {
		}
	}

}
