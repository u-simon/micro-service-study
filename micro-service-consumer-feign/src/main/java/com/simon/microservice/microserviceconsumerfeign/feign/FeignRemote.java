package com.simon.microservice.microserviceconsumerfeign.feign;

import feign.RequestLine;

/**
 * @author fengyue
 * @Date 2019-06-22 16:46
 */
public interface FeignRemote {

    @RequestLine("GET /provider/hello")
    String hello();
}
