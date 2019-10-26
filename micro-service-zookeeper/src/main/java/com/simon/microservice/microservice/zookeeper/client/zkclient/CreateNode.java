package com.simon.microservice.microservice.zookeeper.client.zkclient;

import com.simon.microservice.microservice.zookeeper.api.Constant;
import org.I0Itec.zkclient.ZkClient;

/**
 * @author simon
 * @Date 2019/10/26 14:35
 * @Describe
 */
public class CreateNode {
    /**
     * String create(final String path, Object data, final CreateMode createMode)
     * String create(final String path, Object data, final List<ACL> acl, final CreateMode createMode)
     * void create(final String path, Object data, final CreateMode createMode, final AsyncCallback.StringCallback callback, final Object context)
     * void createEphemeral(final String path)
     * void createEphemeral(final String path, final Object data)
     * void createPersistent(String path)
     * void cratePersistent(String path, boolean createParents)
     * void cratePersistent(String path, Object data)
     * void createPersistent(String path, List<ACL> acl, Object data)
     * String createPersistentSequential(String path, Object data)
     * String createEphemeralSequential(final String path, final Object data)
     */

    public static void main(String[] args) {
        ZkClient zkClient = new ZkClient(Constant.ZOOKEEPER_CONNECTION_ADDRESS, 5000);
        zkClient.createPersistent(Constant.PATH + "/simons", true);
    }
}
