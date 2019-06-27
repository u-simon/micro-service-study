package com.simon.microservice.microserviceconsumerfeign.client;

import org.springframework.stereotype.Component;

/**
 * @author fengyue
 * @Date 2019-06-25 17:54
 */
@Component
public class FeignRemoteClientHystrix implements FeignRemoteClient {
    @Override
    public String hello() {
        return "hystrix";
    }
}
