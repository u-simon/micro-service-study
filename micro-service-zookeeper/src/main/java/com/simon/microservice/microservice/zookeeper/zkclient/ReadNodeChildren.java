package com.simon.microservice.microservice.zookeeper.zkclient;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author simon
 * @Date 2019/10/25 14:19
 * @Describe
 */
public class ReadNodeChildren {
    /**
     * method declare
     *      sync method
     *          List<String> getChildren(final String path, Watcher watcher)
     *          List<String> getChildren(String path, boolean watch)
     *          List<String> getChildren(final String path, Watcher watcher, Stat stat)
     *          List<String> getChildren(String path, boolean watch, Stat stat)
     *       async method
     *          void getChildren(final String path, Watcher watcher, ChildrenCallback cb, Object ctx)
     *          void getChildren(String path, boolean watch, ChildrenCallback cb, Object ctx)
     *          void getChildren(final String path, Watcher watcher, Children2Callback cb, Object ctx)
     *          void getChildren(String path, boolean watch, Children2Callback cb, Object ctx)
     *
     * parameters declare
     *        path : 指定数据节点的路径,即API调用的目的是获取该节点的子节点列表
     *        watcher : 注册的Watcher,一旦在本次子节点获取之后,子节点列表发生变更的话,那么就会向客户端发送通知.改参数允许传入null
     *        watch : 表明是否需要注册一个Watcher,如果这个参数是true,那么Zookeeper客户端会自动使用上下文中提到的那个默认Watcher,如果是false,表明不需要注册Watcher
     *        cb : 注册一个异步回调函数
     *        ctx : 用于传递上下午问信息的对象
     *        stat : 指定数据节点的节点状态信息,用法是在接口中传入一个旧的stat变量,该Stat变量会在方法执行过程中,被来自服务端响应的新Stat对象替换
     */

    public static ZooKeeper zooKeeper;

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        CountDownLatch countDownLatch  = new CountDownLatch(1);
         zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                if (event.getType() == Event.EventType.None && event.getPath() == null){
                    countDownLatch.countDown();
                } else if (event.getType() == Event.EventType.NodeChildrenChanged){
                    try {
                        System.out.println("reGet : " + zooKeeper.getChildren(event.getPath(), true));
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

         countDownLatch.await();
         zooKeeper.create("/simon", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
         zooKeeper.create("/simon/simons", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
         List<String> children = zooKeeper.getChildren("/simon", true);

         System.out.println(children);

         zooKeeper.create("/simon/lemon", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

         zooKeeper.getChildren("/simon", true, new AsyncCallback.Children2Callback(){
             @Override
             public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
                 System.out.println("get children znode result : [response code : "
                         + rc + ", param path : " + path + " ,ctx : " + ctx + ", children list : " + children + ", stat : " + stat);
             }
         }, null);
         Thread.sleep(Integer.MAX_VALUE);
    }
}
