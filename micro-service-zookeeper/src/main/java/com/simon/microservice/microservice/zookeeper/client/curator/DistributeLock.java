package com.simon.microservice.microservice.zookeeper.client.curator;

import com.simon.microservice.microservice.zookeeper.api.Constant;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * @author simon
 * @Date 2019/10/28 17:24
 * @Describe
 */
public class DistributeLock {

	public static void main(String[] args) throws Exception {
		CuratorFramework build = CuratorFrameworkFactory.builder()
				.connectString(Constant.ZOOKEEPER_CONNECTION_ADDRESS).sessionTimeoutMs(5000)
				.retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
		build.start();
		InterProcessMutex interProcessMutex = new InterProcessMutex(build, Constant.PATH);

		CountDownLatch countDownLatch = new CountDownLatch(1);
		for (int i = 0; i < 30; i++) {
			new Thread(() -> {
				try {
					countDownLatch.await();
					interProcessMutex.acquire();
				} catch (Exception e) {
				}

				System.out.println("order number : "
						+ new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()));

				try {
					interProcessMutex.release();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}).start();
		}
		countDownLatch.countDown();

		// 分布式计数器
		String distatomicint = "/distatomicint_path";
		DistributedAtomicInteger atomicInteger =
				new DistributedAtomicInteger(build, distatomicint, new RetryNTimes(3, 1000));
		AtomicValue<Integer> add = atomicInteger.add(8);
		System.out.println("result : " + add.succeeded());

		// 分布式barrier
	}
}
