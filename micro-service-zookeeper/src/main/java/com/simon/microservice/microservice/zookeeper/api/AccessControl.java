package com.simon.microservice.microservice.zookeeper.api;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

/**
 * @author simon
 * @Date 2019/10/25 19:29
 * @Describe
 */
public class AccessControl {
    /**
     * Zookeeper提供了多种权限控制模式
     *          1.world
     *          2.auth
     *          3.digest
     *          4.ip
     *          5.super
     *   method
     *      为当前Zookeeper会话添加权限信息,之后凡是通过该会话对Zookeeper服务端进行的任何操作,都会带上该权限信息
     *      addAuthInfo(String name, byte[] auth)
     *
     *   parameters declare
     *      scheme : 权限控制模式
     *      auth : 具体的权限信息
     *
     *   注意:
     *      对于删除操作而言,其作用范围是其子节点,也就是说,当我们对一个节点添加权限信息后,依然可以自由地删除这个节点,
     *      但是对于这个节点的子节点,就必须使用相应的权限信息才能删除它
     *
     */

    public static void main(String[] args) throws Exception {

        Watcher watcher = event -> {

        };
        ZooKeeper zooKeeper = new ZooKeeper(Constant.ZOOKEEPER_CONNECTION_ADDRESS, 5000, watcher);
        zooKeeper.addAuthInfo("digest", "foo:true".getBytes());
        //创建的时候权限也要设定 不然不起作用
        zooKeeper.create(Constant.PATH, "info".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
        zooKeeper.create(Constant.PATH + "/simons", "info".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);


        ZooKeeper zooKeeper1 = new ZooKeeper(Constant.ZOOKEEPER_CONNECTION_ADDRESS, 5000, watcher);
        zooKeeper1.addAuthInfo("digest", "foo:true".getBytes());
        System.out.println(new String(zooKeeper1.getData(Constant.PATH, false, null)));

        // 使用错误权限信息访问节点
        ZooKeeper zooKeeper2 = new ZooKeeper(Constant.ZOOKEEPER_CONNECTION_ADDRESS, 5000, watcher);
        zooKeeper2.addAuthInfo("digest", "foo:false".getBytes());
        try {
            System.out.println(new String(zooKeeper2.getData(Constant.PATH, false, null)));
        } catch ( Exception e){

        }

        // delete

        try {
            zooKeeper2.delete(Constant.PATH + "/simons", -1);
        } catch (Exception e){
            e.printStackTrace();
        }
        zooKeeper1.delete(Constant.PATH + "/simons", -1);
        System.out.println("成功删除节点 Constant.PATH + \"/simons\", -1");

        zooKeeper2.delete(Constant.PATH, -1);
        System.out.println("成功删除节点/simon");



        Thread.sleep(Integer.MAX_VALUE);
    }
}
