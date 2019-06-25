package com.simon.microservice.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MicroServiceProvider2Application {

    public static void main(String[] args) {
        SpringApplication.run(MicroServiceProvider2Application.class, args);
    }

}
