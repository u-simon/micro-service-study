package com.simon.microservice.microservicezuul.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fengyue
 * @Date 2019-07-02 16:51
 */
@RestController
public class LocalController {

    @GetMapping("/local/{id}")
    public String local(@PathVariable("id") String id){
        return id;
    }
}
