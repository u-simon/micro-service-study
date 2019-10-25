package com.simon.microservice.microservice.zookeeper.zkclient;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author simon
 * @Date 2019/10/24 17:48
 * @Describe
 */
public class RepeatSessionZkClient {
	public static void main(String[] args) throws IOException, InterruptedException {
		CountDownLatch countDownLatch = new CountDownLatch(1);

		String connectString = "127.0.0.1:2181";
		Watcher watcher = new Watcher() {
			@Override
			public void process(WatchedEvent watchedEvent) {
				System.out.println("receive watched event : " + watchedEvent);
				if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
					countDownLatch.countDown();
				}
			}
		};
		ZooKeeper zooKeeper = null;
		zooKeeper = new ZooKeeper(connectString, 5000, watcher);

		countDownLatch.await();
		long sessionId = zooKeeper.getSessionId();
		byte[] sessionPasswd = zooKeeper.getSessionPasswd();

		//use illegal sessionId and sessionPasswd
//		zooKeeper = new ZooKeeper(connectString, 5000, watcher, 1L, "test".getBytes());

		//use correct sessionId and sessionPasswd
		zooKeeper = new ZooKeeper(connectString, 5000, watcher, sessionId, sessionPasswd);

		Thread.sleep(Integer.MAX_VALUE);

	}
}
