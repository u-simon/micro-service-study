package com.simon.microservice.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class MicroServiceEureka3Application {

    public static void main(String[] args) {
        SpringApplication.run(MicroServiceEureka3Application.class, args);
    }

}
