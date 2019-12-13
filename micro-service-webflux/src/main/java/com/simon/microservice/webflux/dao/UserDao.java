package com.simon.microservice.webflux.dao;

import com.simon.microservice.webflux.model.User;

import java.util.Collection;

/**
 * @author simon
 * @Date 2019/12/12 10:46
 * @Describe 一念花开, 一念花落
 */
public interface UserDao {

    Long save(User user);

    Collection<User> queryAll();

    User queryById(Long id);

    Long update(User user);

    Long delete(Long id);
}
