package com.simon.microservice.microservice.zookeeper.client.curator;

import com.simon.microservice.microservice.zookeeper.api.Constant;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author simon
 * @Date 2019/10/26 15:25
 * @Describe
 */
public class CreateConnection {
    /**
     * static CuratorFramework newClient(String connectionString, RetryPolicy retryPolicy)
     * static CuratorFramework newClient(String connectionString, int sessionTimeoutMs, int connectionTimeoutMs, RetryPolicy retryPolicy)
     *
     * parameters declare
     *          retryPolicy : 重试策略,默认主要有四种实现,分别是ExponentialBackoffRetry/RetryNTimes/RetryOneTime/RetryUntilElapsed
     */

    public static void main(String[] args) throws InterruptedException {
        CuratorFramework curatorFramework = CuratorFrameworkFactory
                .newClient(Constant.ZOOKEEPER_CONNECTION_ADDRESS, new ExponentialBackoffRetry(1000, 3));
        CuratorFramework curatorFramework1 = CuratorFrameworkFactory.newClient(Constant.ZOOKEEPER_CONNECTION_ADDRESS,
                5000, 3000, new ExponentialBackoffRetry(1000, 3));

        curatorFramework.start();

        //fluent风格(就是build模式)
        CuratorFramework build = CuratorFrameworkFactory.builder().connectString(Constant.ZOOKEEPER_CONNECTION_ADDRESS).sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
        build.start();


        //创建包含隔离命名空间的会话(即根路径
        CuratorFramework base = CuratorFrameworkFactory.builder().connectString(Constant.ZOOKEEPER_CONNECTION_ADDRESS).sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3)).namespace("base").build();
        base.start();


        Thread.sleep(Integer.MAX_VALUE);
    }
}
