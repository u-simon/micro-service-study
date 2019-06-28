package com.simon.microservice.microserviceconsumerfeign.client;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author fengyue
 * @Date 2019-06-28 14:12
 */
@Component
public class FeignRemoteClientHystrixFactory implements FallbackFactory<FeignRemoteClient> {
    @Override
    public FeignRemoteClient create(Throwable throwable) {
        return new FeignRemoteClient() {
            @Override
            public String hello() {
                return "hystrixFactory";
            }
        };
    }
}
