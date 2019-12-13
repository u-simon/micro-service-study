package com.simon.microservice.webflux.dao.impl;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import com.simon.microservice.webflux.dao.UserDao;
import com.simon.microservice.webflux.model.User;

/**
 * @author simon
 * @Date 2019/12/12 10:46
 * @Describe 一念花开, 一念花落
 */
@Repository
public class UserDaoImpl implements UserDao {

	Map<Long, User> userDb = new ConcurrentHashMap<>();
	AtomicLong idGenerator = new AtomicLong(0);

    @Override
    public Long save(User user) {
        long id = idGenerator.incrementAndGet();
        user.setId(id);
        userDb.put(id, user);
        return id;
    }

    @Override
    public Collection<User> queryAll() {
        return userDb.values();
    }

    @Override
    public User queryById(Long id) {
        return userDb.get(id);
    }

    @Override
    public Long update(User user) {
        userDb.put(user.getId(), user);
        return user.getId();
    }

    @Override
    public Long delete(Long id) {
        userDb.remove(id);
        return id;
    }
}
