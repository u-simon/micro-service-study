package com.simon.microservice.webflux.service;

import com.simon.microservice.webflux.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author simon
 * @Date 2019/12/12 11:18
 * @Describe 一念花开, 一念花落
 */
public interface UserService {

    Mono<Long> save(User user);

    Mono<User> queryById(Long id);

    Flux<User> queryAll();

    Mono<Long> update(User user);

    Mono<Long> delete(Long id);
}
