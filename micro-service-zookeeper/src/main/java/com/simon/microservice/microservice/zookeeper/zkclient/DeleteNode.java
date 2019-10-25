package com.simon.microservice.microservice.zookeeper.zkclient;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author simon
 * @Date 2019/10/25 11:32
 * @Describe
 */
public class DeleteNode {

    /**
     * 同步删除 --> void delete(final String path, int version)
     * 异步删除 --> void delete(final String path, int version, VoidCallback cb, Object ctx)
     *
     * parameters declare
     *          1.path --> 指定数据节点的节点路径,即API调用的目的是删除该节点
     *          2.version --> 指定节点数据的版本,即表明本次删除
     *          3.cb --> 注册一个异步回调函数
     *          4.ctx --> 用于传递上下文信息的对象
     *
     *  注意：
     *      在Zookeeper中,只允许删除叶子节点, 也就是说,如果一个节点存在至少一个子节点的话,那么该节点将无法被直接删除,必须先删除掉其所有子节点
     */

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                if (Event.KeeperState.SyncConnected == event.getState()){
                    countDownLatch.countDown();
                }
            }
        });
        countDownLatch.await();
        zooKeeper.delete("/simon", 0);
    }
}
