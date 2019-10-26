package com.simon.microservice.microservice.zookeeper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.time.LocalTime;

@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
public class MicroServiceZookeeperApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroServiceZookeeperApplication.class, args);
	}

}
