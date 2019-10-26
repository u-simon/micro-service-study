package com.simon.microservice.microservice.zookeeper.client.zkclient;

import com.simon.microservice.microservice.zookeeper.api.Constant;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

/**
 * @author simon
 * @Date 2019/10/26 15:02
 * @Describe
 */
public class GetNodeData {
    /**
     * T readData(String path)
     * T readData(String path, boolean returnNullIfPathNotExists)
     * T readData(String path, Stat stat)
     *
     */

    public static void main(String[] args) throws InterruptedException {
        ZkClient zkClient = new ZkClient(Constant.ZOOKEEPER_CONNECTION_ADDRESS, 5000);

        zkClient.createEphemeral(Constant.PATH,"123");

        //监听数据的变化
        zkClient.subscribeDataChanges(Constant.PATH, new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) throws Exception {
                System.out.println("node " + s + " change, new Data : " + o);
            }

            @Override
            public void handleDataDeleted(String s) throws Exception {
                System.out.println("node " + s + " deleted ");
            }
        });
        System.out.println(zkClient.readData(Constant.PATH).toString());

        zkClient.writeData(Constant.PATH, "456");

        Thread.sleep(1000);

        zkClient.delete(Constant.PATH);

        Thread.sleep(Integer.MAX_VALUE);

    }
}
