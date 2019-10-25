package com.simon.microservice.microservice.zookeeper.zkclient;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.*;

/**
 * @author simon
 * @Date 2019/10/24 19:27
 * @Describe
 */
public class CreateNode {
    /**
     * String create(final String path, byte data[], List<ACL> acl, CreateMode createMode) 同步创建
     *
     * void create(final String path, byte data[], List<ACL> acl, CreateMode createMode, StringCallback cb, Object ctx)
     *
     * parameters
     *  -- path: 需要创建的数据节点的节点路径 eg:/zk-book/foo
     *  -- data[]: 一个字节数组,是节点创建后的初始内容
     *  -- acl: 节点的ACL策略
     *  -- createMode: 节点类型，是一个枚举类型,通常有4种可选的节点类型
     *                  1) 持久(persistent)
     *                  2) 持久顺序(persistent_sequential)
     *                  3) 临时(ephemeral)
     *                  4) 临时顺序(ephemeral_sequential)
     *  -- cb 注册一个异步回调函数,当服务端节点创建完毕后,Zookeeper客户端就会自动调用这个方法这样就可以处理相关的业务逻辑了
     *  -- ctx 用于传递一个对象,可以在回调方法执行的时候使用,通常是放一个上下文(Context)信息
     *
     *  注意 :
     *      不支持递归创建
     */

    public static void main(String[] args) throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ZooKeeper zooKeeper =  new ZooKeeper("127.0.0.1:2181", 5000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                if (Event.KeeperState.SyncConnected == event.getState()){
                    countDownLatch.countDown();
                }
            }
        });
        countDownLatch.await();
        // sync create
        String path1 = zooKeeper
                .create("/zk-test-ephemeral-", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("success create ephemeral zNode : " + path1);
        String path2 = zooKeeper
                .create("/zk-test-ephemeral-/sequential-", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        System.out.println("success create ephemeral sequential znode : " + path2);

        // async create

        zooKeeper.create("/zk-test-async-",
                "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, new AsyncCallback.StringCallback(){
            @Override
            public void processResult(int rc, String path, Object ctx, String name) {
                System.out.println("async create ephemeral znode : " + rc   + " -- " + ctx.toString() + " --- " + name   );
            }
        }, "123");

        zooKeeper.create("/zk-async-ephemeral-sequential",
                "". getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL, new AsyncCallback.StringCallback(){
            /**
             *
             * @param rc --> resultCode 服务端响应码,客户端可以从这个响应码中识别出API调用的结果,常见的响应码
             *           0    --> 接口调用成功(OK)
             *           -4   --> 客户端和服务端连接已断开(ConnectionLoss)
             *           -110 --> 指定节点已存在(NodeExist)
             *           -112 --> 会话已过期(sessionExpired)
             * @param path 接口调用时传入API的数据节点的节点路径参数值
             * @param ctx 接口调用时传入API的ctx参数值
             * @param name 实际在服务端创建的节点名
             */
            @Override
            public void processResult(int rc, String path, Object ctx, String name) {
                System.out.println("success async create ephemeral sequential zNode : " + name + "--->" + rc + " --->" + ctx.toString());
            }
        }, "async");


        Thread.sleep(Integer.MAX_VALUE);
    }
}
