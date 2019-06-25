package com.simon.microservice.provider.controller;

import com.netflix.discovery.EurekaClient;
import com.simon.microservice.provider.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * @author fengyue
 * @Date 2019-06-19 14:53
 */
@RestController
@RequestMapping("/provider")
public class ProviderController {
    @Autowired
    EurekaClient eurekaClient;

    @Value("${server.port}")
    private String serverPost;

    @RequestMapping("/hello")
    public String hello() {
        return "Hello" + serverPost;
    }


    @GetMapping("/data")
    public User getData(@RequestParam(value = "name") String name) {
        return new User(1L, name, 20);
    }

    @GetMapping("/data/{name}")
    public String getData2(@PathVariable(value = "name") String name) {
        return name ;
    }

    @RequestMapping("/infos")
    public Object serviceUrl() {
        return eurekaClient.getInstancesByVipAddress("micro-service-provider", false);
    }

    @PostMapping("/save")
    public Long addData(@RequestBody User user){
        System.out.println(user);
        return 100L;
    }
}
