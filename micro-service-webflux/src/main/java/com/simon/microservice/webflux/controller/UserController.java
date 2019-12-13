package com.simon.microservice.webflux.controller;

import com.simon.microservice.webflux.model.User;
import com.simon.microservice.webflux.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author simon
 * @Date 2019/12/12 16:14
 * @Describe 一念花开, 一念花落
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;


    @GetMapping("/{id}")
    public Mono<User> queryById(@PathVariable("id") Long id){
        return userService.queryById(id);
    }


    @GetMapping()
    public Flux<User> queryAll(){
        return userService.queryAll();
    }

    @PostMapping
    public Mono<Long> save( @RequestBody User user){
        return userService.save(user);
    }

    @PutMapping()
    public Mono<Long> update(User user){
        return userService.update(user);
    }

    @DeleteMapping("/{id}")
    public Mono<Long> delete(@PathVariable("id") Long id){
        return userService.delete(id);
    }
}
