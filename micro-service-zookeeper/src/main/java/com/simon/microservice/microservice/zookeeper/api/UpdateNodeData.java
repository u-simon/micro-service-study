package com.simon.microservice.microservice.zookeeper.api;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

/**
 * @author simon
 * @Date 2019/10/25 15:24
 * @Describe
 */
public class UpdateNodeData {

    /**
     * Stat setData(final String path, byte[] data, int version)
     * void setData(final String path, byte[] data, int version, StatCallback cb, Object ctx)
     *
     * parameters declare
     *      path : 指定数据节点的节点路径
     *      data : 一个字节数组,即需要使用该数据来内容来覆盖节点现在的数据内容
     *      version : 指定节点的数据版本
     *      cb : 注册一个异步回调函数
     *      ctx : 用于传递上下文信息的对象
     */

    public static void main(String[] args) throws Exception {

        CountDownLatch countDownLatch = new CountDownLatch(1);

        ZooKeeper zooKeeper = new ZooKeeper(Constant.ZOOKEEPER_CONNECTION_ADDRESS, 5000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                if (Event.KeeperState.SyncConnected == event.getState()){
                    if (Event.EventType.None == event.getType() && event.getPath() == null){
                        countDownLatch.countDown();
                    }
                }
            }
        });
        countDownLatch.await();
        zooKeeper.create(Constant.PATH, "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        zooKeeper.getData(Constant.PATH, true, null);

        Stat stat = zooKeeper.setData(Constant.PATH, "456".getBytes(), -1);
        System.out.println(stat.getCzxid() + ", " + stat.getMzxid() + ", " + stat.getVersion());

        Stat stat2 = zooKeeper.setData(Constant.PATH, "456".getBytes(), stat.getVersion());
        System.out.println(stat2.getCzxid() + ", " + stat2.getMzxid() + ", " + stat2.getVersion());

        zooKeeper.setData(Constant.PATH, "234".getBytes(), stat2.getVersion(), new AsyncCallback.StatCallback(){
            @Override
            public void processResult(int rc, String path, Object ctx, Stat stat) {
                if (rc == 0){
                    System.out.println(stat.getCzxid() + ", " + stat.getMzxid() + ", " + stat.getVersion());
                }
            }
        }, null);

        try {
            // 会报错 BadVersion
            zooKeeper.setData(Constant.PATH, "456".getBytes(), stat.getVersion());
        } catch (Exception e){
            e.printStackTrace();
        }

        Thread.sleep(Integer.MAX_VALUE);
    }
}
