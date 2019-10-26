package com.simon.microservice.microservice.zookeeper.client.zkclient;

import com.simon.microservice.microservice.zookeeper.api.Constant;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;

/**
 * @author simon
 * @Date 2019/10/26 14:48
 * @Describe
 */
public class GetNodeChildren {
    /**
     * List<String> getChildren(String path)
     *
     * 注册监听
     * List<String> subscribeChildChanges(String path, IZkChildListener listener)
     *
     * 结论:
     *  1.客户端可以对一个不存在的节点进行子节点变更的监听
     *  2.一旦客户端对一个节点注册了子节点列表变更监听之后,那么当该节点的子节点列表发生变更的时候,服务端都会通知客户端,并将最新的子节点列表发送给客户端
     *  3.该节点本身的创建或删除也会通知到客户端
     * 注意:
     *      另外,还需要明确的一点是,和Zookeeper原生提供的Watcher不同的是,zkClient的Listener不是一次性的,客户端只需要注册一次就会一直生效
     */

    public static void main(String[] args) throws InterruptedException {
        ZkClient zkClient = new ZkClient(Constant.ZOOKEEPER_CONNECTION_ADDRESS, 5000);

        zkClient.subscribeChildChanges(Constant.PATH, new IZkChildListener() {
            @Override
            public void handleChildChange(String s, List<String> list) throws Exception {
                System.out.println(s + " 's child changed , currentChildren : " + list);
            }
        });

        zkClient.createPersistent(Constant.PATH);
        Thread.sleep(1000);
        System.out.println(zkClient.getChildren(Constant.PATH));
        Thread.sleep(1000);

        zkClient.createPersistent(Constant.PATH + "/simons");
        Thread.sleep(1000);

        zkClient.delete(Constant.PATH + "/simons");
        Thread.sleep(1000);

        zkClient.delete(Constant.PATH);

        Thread.sleep(Integer.MAX_VALUE);

    }
}
