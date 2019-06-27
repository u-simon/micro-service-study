package com.simon.microservice.consumer.controller;

import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.simon.microservice.consumer.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * @author fengyue
 * @Date 2019-06-19 15:03
 */
@RestController
@RequestMapping("/consumer")
public class ConsumerController {

    @Autowired
    RestTemplate restTemplate;

    private static final String PROVIDER_URL = "http://micro-service-provider";

    @RequestMapping("/callHello")
    @HystrixCommand(fallbackMethod = "defaultCallHello", commandProperties = {@HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")})
    public String callHello() {
        return restTemplate.getForObject(PROVIDER_URL + "/provider/hello", String.class);
    }

    @GetMapping("/data")
    public User getData(@RequestParam("name") String name) {
        // return restTemplate.getForObject(PROVIDER_URL + "/provider/data?name=" + name, User.class);
        ResponseEntity<User> forEntity = restTemplate.getForEntity(PROVIDER_URL + "/provider/data?name=" + name, User.class);
        if (forEntity.getStatusCodeValue() == 200){
            return forEntity.getBody();
        }
        return null;
    }

    public String defaultCallHello(){
        return "fail";
    }

    @GetMapping("/data/{name}")
    public String getData2(@PathVariable("name") String name) {
        return restTemplate.getForObject(PROVIDER_URL + "/provider/data/" + name, String.class);
    }

    @GetMapping("/add")
    public Long add (){
        User user = new User();
        user.setName("lemon");
        user.setAge(23);
        return restTemplate.postForObject(PROVIDER_URL + "/provider/save", user, Long.class);
    }
    @Autowired
    LoadBalancerClient loadBalancerClient;

    @RequestMapping("/choose")
    public Object chooseUrl(){
        return loadBalancerClient.choose("micro-service-provider");
    }

}
