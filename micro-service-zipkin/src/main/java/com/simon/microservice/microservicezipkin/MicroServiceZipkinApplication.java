package com.simon.microservice.microservicezipkin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zipkin2.server.internal.EnableZipkinServer;

@SpringBootApplication
@EnableZipkinServer
public class MicroServiceZipkinApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroServiceZipkinApplication.class, args);
	}

}
