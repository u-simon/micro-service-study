package com.simon.microservice.provider.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fengyue
 * @Date 2019-06-20 16:03
 */
@RestController
@RequestMapping("/provider")
@Slf4j
public class Provider2Controller {

    @Value("${server.port}")
    String serverPort;

    @GetMapping("/hello")
    public String hell0(){
        log.info("hello service");
        return "Hello" + serverPort;
    }
}
