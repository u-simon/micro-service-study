package com.simon.microservice.microservice.zookeeper.client.zkclient;

import com.simon.microservice.microservice.zookeeper.api.Constant;
import org.I0Itec.zkclient.ZkClient;

/**
 * @author simon
 * @Date 2019/10/26 14:44
 * @Describe
 */
public class DeleteNode {

    /**
     * boolean delete(final String path)
     * delete(final String path, final AsyncCallback.VoidCallback callback, final Object context)
     * boolean deleteRecursive(String path)递归删除
     */
    public static void main(String[] args) {
        ZkClient zkClient = new ZkClient(Constant.ZOOKEEPER_CONNECTION_ADDRESS, 5000);
        zkClient.deleteRecursive(Constant.PATH);
    }
}
