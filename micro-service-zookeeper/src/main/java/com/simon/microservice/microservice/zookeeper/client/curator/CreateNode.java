package com.simon.microservice.microservice.zookeeper.client.curator;

import com.simon.microservice.microservice.zookeeper.api.Constant;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * @author simon
 * @Date 2019/10/28 15:12
 * @Describe
 */
public class CreateNode {

    public static void main(String[] args) throws Exception {
        CuratorFramework build = CuratorFrameworkFactory.builder().connectString(Constant.ZOOKEEPER_CONNECTION_ADDRESS)
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();

        build.start();

        build.create().creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath("/simon/simons", "info".getBytes());

    }
}
