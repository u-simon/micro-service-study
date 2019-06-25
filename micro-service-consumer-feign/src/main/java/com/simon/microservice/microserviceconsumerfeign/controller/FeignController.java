package com.simon.microservice.microserviceconsumerfeign.controller;

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
public class FeignController {

    @Autowired
    FeignRemoteClient feignRemoteClient;

    @RequestMapping("/callHello")
    public String callHello(){
        return feignRemoteClient.hello();
    }
}
