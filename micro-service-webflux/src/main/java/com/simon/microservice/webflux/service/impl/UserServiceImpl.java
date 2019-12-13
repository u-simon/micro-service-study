package com.simon.microservice.webflux.service.impl;

import com.simon.microservice.webflux.dao.UserDao;
import com.simon.microservice.webflux.model.User;
import com.simon.microservice.webflux.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;

import java.util.function.Consumer;

/**
 * @author simon
 * @Date 2019/12/12 11:18
 * @Describe 一念花开, 一念花落
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserDao userDao;

    /**
     * Mono 实现发布者,并返回0/1个元素
	 * Mono 是响应流Publisher具有基础Rx操作符，可以成功发布元素或错误
     *  method:
     *      Mono.create(): 使用MonoSink来创建Mono
     *      Mono.justOrEmpty(): 从一个Optional对象或null对象中创建Mono
     *      Mono.error(): 创建一个只包含错误信息的Mono
     *      Mono.never(): 创建一个不包含任何消息通知的Mono
     *      Mono.delay(): 在指定的延迟时间之后,创建一个Mono,产生数字0作为唯一值
     * Flux 实现发布者,返回n个元素,即List列表对象
     *
     * 直接使用Mono和Flux是非阻塞写法,相当于回调方式,利用函数式可以减少了回调,因此会看不到相关接口
     */

	@Override
	public Mono<Long> save(User user) {
		return Mono.create(longMonoSink -> longMonoSink.success(userDao.save(user)));
	}

	@Override
	public Mono<User> queryById(Long id) {
		return Mono.create(new Consumer<MonoSink<User>>() {
			@Override
			public void accept(MonoSink<User> userMonoSink) {
				userMonoSink.success(userDao.queryById(id));
			}
		});
	}

	@Override
	public Flux<User> queryAll() {
		return Flux.fromIterable(userDao.queryAll());
	}

	@Override
	public Mono<Long> update(User user) {
		return Mono.create(new Consumer<MonoSink<Long>>() {
			@Override
			public void accept(MonoSink<Long> longMonoSink) {
				longMonoSink.success(userDao.update(user));
			}
		});
	}

	@Override
	public Mono<Long> delete(Long id) {
		return Mono.create(new Consumer<MonoSink<Long>>() {
			@Override
			public void accept(MonoSink<Long> longMonoSink) {
				longMonoSink.success(userDao.delete(id));
			}
		});
	}
}
