package com.simon.microservice.microservice.zookeeper.client.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;

import com.simon.microservice.microservice.zookeeper.api.Constant;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * @author simon
 * @Date 2019/10/28 16:39
 * @Describe
 */
public class NodeCacheTest {

    /**
     * cache分为两类监听类型:节点监听和子节点监听
     *
     *  public NodeCache(CuratorFramework client, String path)
     *  public NodeCache(CuratorFramework client, String path, boolean dataIsCompressed)
     *  parameters declare
     *         client : 客户端实例
     *         path : 数据节点的节点路径
     *         dataIsCompressed : 是否进行数据压缩
     *  NodeCache 定义了事件处理的回调接口 NodeCacheListener
     *      public void nodeChanged() throws Exception  当数据节点的内容发生变化的时候,就会回调该方法
     *
     */

    public static void main(String[] args) throws Exception {
        CuratorFramework build = CuratorFrameworkFactory.builder()
                .connectString(Constant.ZOOKEEPER_CONNECTION_ADDRESS)
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        build.start();
        build.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(Constant.PATH, "info".getBytes());
        NodeCache nodeCache = new NodeCache(build, Constant.PATH, false);
        nodeCache.start();
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                ChildData currentData = nodeCache.getCurrentData();
                if (currentData != null)
                    System.out.println(new String(nodeCache.getCurrentData().getData()));
            }
        });
        build.setData().forPath(Constant.PATH, "123".getBytes());
        Thread.sleep(1000);
        build.delete().deletingChildrenIfNeeded().forPath(Constant.PATH);
        Thread.sleep(Integer.MAX_VALUE);
    }
}
