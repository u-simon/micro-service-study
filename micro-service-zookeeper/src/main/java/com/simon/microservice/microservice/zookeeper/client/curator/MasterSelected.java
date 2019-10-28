package com.simon.microservice.microservice.zookeeper.client.curator;

import com.simon.microservice.microservice.zookeeper.api.Constant;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author simon
 * @Date 2019/10/28 17:14
 * @Describe
 */
public class MasterSelected {

    public static void main(String[] args) throws InterruptedException {
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(Constant.ZOOKEEPER_CONNECTION_ADDRESS)
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();

        client.start();

        LeaderSelector selector = new LeaderSelector(client, Constant.PATH, new LeaderSelectorListenerAdapter() {
            @Override
            public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
                System.out.println("成为Master角色");
                Thread.sleep(3000);
                System.out.println("完成Master操作,释放Master权利");
            }
        });

        selector.autoRequeue();
        selector.start();

        Thread.sleep(Integer.MAX_VALUE);
    }
}
