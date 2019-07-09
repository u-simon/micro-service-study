package com.simon.microservice.microserviceconsumerfeign.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simon.microservice.microserviceconsumerfeign.client.FeignRemoteClient;

/**
 * @author fengyue
 * @Date 2019-06-22 14:41
 */
@RestController
@RequestMapping("/feign")
@Slf4j
public class FeignController {

    @Autowired
    FeignRemoteClient feignRemoteClient;

    @RequestMapping("/callHello")
    public String callHello(){
        log.info("call hello");
        return feignRemoteClient.hello();
    }
}
