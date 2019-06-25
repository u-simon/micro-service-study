package com.simon.microservice.microserviceconsumerfeign.client;

import com.simon.microservice.microserviceconsumerfeign.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author fengyue
 * @Date 2019-06-22 14:35
 */
@FeignClient(value = "micro-service-provider", path = "/provider", configuration = FeignConfig.class)
//@Component
public interface FeignRemoteClient {
    @GetMapping("/hello")
    String hello();
}
