package com.simon.microservice.provider.controller;

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
public class Provider2Controller {

    @Value("${server.port}")
    String serverPort;

    @GetMapping("/hello")
    public String hell0(){
        return "Hello" + serverPort;
    }
}
