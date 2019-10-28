package com.simon.microservice.microservice.zookeeper.client.curator;

import com.simon.microservice.microservice.zookeeper.api.Constant;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author simon
 * @Date 2019/10/28 16:02
 * @Describe
 */
public class AsyncMethod {

    /**
     * 异步接口
     *  BackgroundCallback
     *      public void processResult(CuratorFramework client, CuratorEvent event) throws Exception
     *
     *      parameter declare
     *          client : 当前客户端实例
     *          event : 服务端事件
     *
     *        CuratorEventType 事件类型
     *          create/delete/exists/get_data/set_data/children/sync/get_acl/watched/closing
     */

    public static void main(String[] args) throws Exception {
        String path = "/simon";

        CuratorFramework build = CuratorFrameworkFactory.builder().connectString(Constant.ZOOKEEPER_CONNECTION_ADDRESS)
                .sessionTimeoutMs(5000).retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        build.start();
        CountDownLatch countDownLatch = new CountDownLatch(2);
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        build.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).inBackground(new BackgroundCallback(){
            @Override
            public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                System.out.println("event code " + event.getResultCode() + ", type : " + event.getType());
                System.out.println("Thread name  : " + Thread.currentThread().getName() );
                countDownLatch.countDown();
            }
        }, executorService).forPath(path, "info".getBytes());

        build.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).inBackground(new BackgroundCallback(){
            @Override
            public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                System.out.println("event code " + event.getResultCode() + ", type : " + event.getType());
                System.out.println("Thread name  : " + Thread.currentThread().getName() );
                countDownLatch.countDown();
            }
        }).forPath(path, "info".getBytes());

        countDownLatch.await();
        executorService.shutdown();
    }
}
