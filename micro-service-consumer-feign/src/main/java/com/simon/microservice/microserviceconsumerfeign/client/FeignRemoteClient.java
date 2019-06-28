package com.simon.microservice.microserviceconsumerfeign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.simon.microservice.microserviceconsumerfeign.config.FeignConfig;

/**
 * @author fengyue
 * @Date 2019-06-22 14:35
 */
@FeignClient(value = "micro-service-provider", path = "/provider",
		configuration = FeignConfig.class, fallbackFactory = FeignRemoteClientHystrixFactory.class)
// configuration = FeignConfig.class, fallback = FeignRemoteClientHystrix.class)
public interface FeignRemoteClient {
	@GetMapping("/hello")
	String hello();
}
