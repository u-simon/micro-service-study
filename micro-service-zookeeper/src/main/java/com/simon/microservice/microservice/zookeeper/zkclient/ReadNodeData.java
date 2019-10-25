package com.simon.microservice.microservice.zookeeper.zkclient;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

/**
 * @author simon
 * @Date 2019/10/25 15:01
 * @Describe
 */
public class ReadNodeData {
    /**
     * byte[] getData(final String path, Watcher watcher, Stat stat )
     * byte[] getData(String path, boolean watch, Stat stat)
     * void getData(final String path, Watcher watcher, DataCallback cb, Object ctx)
     * void getData(String path, boolean watch, DataCallback cb, Object ctx)
     *
     * parameters declare
     *      path : 指定数据的节点路径,即API调用目的是获取该节点的数据内容
     *      watcher : 注册的Watcher
     *      stat : 指定数据节点的节点状态信息
     *      watch : 表明是否需要注册一个Watcher
     *      cb : 注册一个异步回调函数
     *      ctx : 要不传递上下文信息的对象
     */
    private static ZooKeeper zooKeeper = null;
    public static void main(String[] args) throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        Stat stat = new Stat();
        zooKeeper = new ZooKeeper(Constant.ZOOKEEPER_CONNECTION_ADDRESS, 5000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                if (Event.EventType.None == event.getType() && event.getPath() == null){
                    countDownLatch.countDown();
                } else if (Event.EventType.NodeDataChanged == event.getType()){
                    try {
                        System.out.println(new String(zooKeeper.getData(event.getPath(), true, stat)));
                        System.out.println(stat.getCzxid() + ", " + stat.getMzxid() + ", " + stat.getVersion());
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        zooKeeper.create("/simon", "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println(new String(zooKeeper.getData("/simon", true, stat)));
        System.out.println(stat.getCzxid() + ", " + stat.getMzxid() + ", " + stat.getVersion());


        zooKeeper.getData("/simon", true, new AsyncCallback.DataCallback(){
            @Override
            public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
                System.out.println(new String(data));
                System.out.println(stat.getCzxid() + ", " + stat.getMzxid() + ", " + stat.getVersion());
            }
        }, null);

        zooKeeper.setData("/simon", "456".getBytes(), -1);

        Thread.sleep(Integer.MAX_VALUE);
    }
}
