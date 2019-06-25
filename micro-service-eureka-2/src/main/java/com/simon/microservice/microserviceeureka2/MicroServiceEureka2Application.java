package com.simon.microservice.microserviceeureka2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class MicroServiceEureka2Application {

    public static void main(String[] args) {
        SpringApplication.run(MicroServiceEureka2Application.class, args);
    }

}
