package com.simon.microservice.microservice.zookeeper.zkclient;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.*;

/**
 * @author simon
 * @Date 2019/10/25 16:44
 * @Describe
 */
public class ExistNode {

    /**
     * Stat exists(final String path, Watcher watcher)
     * Stat exists(String path, boolean watch)
     * void exists(final String path, Watcher watcher, StatCallback cb, Object ctx)
     * void exists(String path, boolean watch, StatCallback cb, Object ctx)
     *
     * parameters declare
     *      path : 指定数据节点的节点路径
     *      watcher : 注册的Watcher,用于监听以下三类事件 节点创建/节点删除/节点更新
     *      watch : 指定是否复用Zookeeper中默认的Watcher
     *      cb : 注册一个异步回调函数
     *      ctx : 用于传递上下文信息的对象
     */
    static ZooKeeper zooKeeper;
    public static void main(String[] args) throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        zooKeeper = new ZooKeeper(Constant.ZOOKEEPER_CONNECTION_ADDRESS, 5000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                try {
                    if (Event.KeeperState.SyncConnected == event.getState()){
                        if (event.getType() == Event.EventType.None && event.getPath() == null){
                            countDownLatch.countDown();
                        }else if (event.getType() == Event.EventType.NodeCreated){
                            System.out.println("Node : " + event.getPath() + " create " );
                            zooKeeper.exists(event.getPath(), true);
                        } else if (event.getType() == Event.EventType.NodeDeleted){
                            System.out.println("Node : " + event.getPath() + " delete " );
                            zooKeeper.exists(event.getPath(), true);
                        } else if (event.getType() == Event.EventType.NodeDataChanged){
                            System.out.println("Node : " + event.getPath() + " dataChanged " );
                            zooKeeper.exists(event.getPath(), true);
                        }
                    }
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        countDownLatch.await();

        zooKeeper.exists(Constant.PATH, true);

        zooKeeper.create(Constant.PATH, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        zooKeeper.setData(Constant.PATH,"456".getBytes(),-1);

        zooKeeper.delete(Constant.PATH, -1);

        Thread.sleep(Integer.MAX_VALUE);
    }
}
