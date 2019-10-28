package com.simon.microservice.microservice.zookeeper.client.curator;

import com.simon.microservice.microservice.zookeeper.api.Constant;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * @author simon
 * @Date 2019/10/28 15:31
 * @Describe
 */
public class UpdateNodeData {


	public static void main(String[] args) throws Exception {
		CuratorFramework client = CuratorFrameworkFactory.builder()
				.connectString(Constant.ZOOKEEPER_CONNECTION_ADDRESS).sessionTimeoutMs(5000)
				.retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
		client.start();

		String path = "/simon";

		client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path,
				"info".getBytes());

        Stat stat = new Stat();
        System.out.println(new String( client.getData().storingStatIn(stat).forPath(path)));
        client.setData().withVersion(stat.getVersion()).forPath(path, "123".getBytes());
        System.out.println(new String(client.getData().storingStatIn(stat).forPath(path)));
	}
}
