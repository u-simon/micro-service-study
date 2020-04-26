package com.simon.microservice.elasticsearch;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author simon
 */
@SpringBootApplication
public class MicroServiceElasticsearchApplication {

	public static void main(String[] args) throws Exception {
		/**
		 * 浏览器数据url之后经历的步骤 1.浏览器向dns服务器发送请求解析url中的ip地址 2.拿到解析出来的ip之后 根据ip和默认的80端口 和服务器简历tcp连接
		 * 3.浏览器发出读文件的http请求,请求的报文作为tcp三次握手的第三个报文发送到服务器 4.服务器对浏览器的请求作出相应,并把html发送给浏览器 5.释放tcp连接
		 * 6.浏览器渲染html
		 *
		 *
		 *
		 * 表级锁：开销小，加锁快，不会出现死锁，锁定粒度大，发生锁冲突的概率最大，并发度最低 行级锁：开销大，加锁慢，会出现死锁，锁定粒度小，发生锁冲突的概率最小，并发度最高
		 * 页级锁：开销和加锁时间介于行锁和表锁之间，会出现死锁，锁定粒度介于表锁和行锁之间，并发粒度一般
		 * 从锁的角度来看，表级锁更适合已查询为主，只有与少量按索引条件更新数据的应用，而行锁更适合有大量 按索引条件并发更新少量不同数据,同时又有并发查询的应用
		 *
		 *
		 * 共享锁 排它锁 意向共享锁 意向排它锁
		 *
		 * 如果一个事务请求的锁模式与当前的锁兼容，innodb就将请求的锁授予该事务，反之 如果两者不兼容，该事务就要等待锁释放
		 *
		 * 意向锁是innodb自动加的 不需要用户干预
		 *
		 * innodb行锁是通过给索引上的索引项加锁来实现的,只有通过索引条件检索数据,innodb才会使用行锁,否则直接使用表锁
		 *
		 */

		SpringApplication.run(MicroServiceElasticsearchApplication.class, args);
		ReentrantLock reentrantLock = new ReentrantLock(true);
		// reentrantLock.lock();
		reentrantLock.tryLock();
		reentrantLock.tryLock(1, TimeUnit.SECONDS);
		reentrantLock.unlock();

		Condition condition = reentrantLock.newCondition();
		condition.await();
		condition.signal();
		condition.signalAll();

		HashMap<String, String> map = new HashMap<>();
		map.put("a", "A");
		map.get("a");

		ArrayList<String> list = new ArrayList<>(1);
		list.add("a");
		list.get(0);

		ConcurrentHashMap<String, Object> concurrentHashMap = new ConcurrentHashMap<>();
		concurrentHashMap.put("a", "A");
		concurrentHashMap.get("a");

		ThreadLocal<String> threadLocal = new ThreadLocal<>();
		threadLocal.set("a");
		threadLocal.get();
		threadLocal.remove();

		new Thread();

		InheritableThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal<>();
		inheritableThreadLocal.set("a");
		inheritableThreadLocal.get();
		inheritableThreadLocal.remove();

		LinkedList<String> linkedList = new LinkedList<>();
		linkedList.add("a");
		linkedList.get(0);

		System.gc();
		Runtime.getRuntime().runFinalization();

		System.in.read();
		System.out.println(tableSizeFor(10000) * 0.75);

	}

	static final int tableSizeFor(int cap) {
		int n = cap - 1;
		n |= n >>> 1;
		n |= n >>> 2;
		n |= n >>> 4;
		n |= n >>> 8;
		n |= n >>> 16;
		return (n < 0) ? 1 : (n >= Integer.MAX_VALUE) ? Integer.MAX_VALUE : n + 1;
	}


}
