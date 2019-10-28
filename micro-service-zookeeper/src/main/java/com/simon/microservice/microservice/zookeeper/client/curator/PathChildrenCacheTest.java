package com.simon.microservice.microservice.zookeeper.client.curator;

import com.simon.microservice.microservice.zookeeper.api.Constant;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * @author simon
 * @Date 2019/10/28 16:55
 * @Describe
 */
public class PathChildrenCacheTest {

    /**
     * public PathChildrenCache(CuratorFramework client, String path, boolean cacheData)
     *
     * public PathChildrenCache(CuratorFramework client, String path, boolean cacheData, ThreadFactory threadFactory);
     *
     * public PathChildrenCache(CuratorFramework client, String path, boolean cacheData, boolean dataIsCompressed, ThreadFactory threadFactory)
     *
     * public PathChildrenCache(CuratorFramework client, String path, boolean cacheData, boolean dataIsCompressed, final ExecutorService executorService)
     *
     * public PathChildrenCache(CuratorFramework client, String path, boolean cacheData, boolean dataIsCompressed, final CloseableExecutorService executorService)
     *
     * parameters declare :
     *          cacheData : 用于配置是否把节点内容缓存起来, true : 客户端在接受到节点列表变更的同时,也能获取到节点的数据内容, false : 不能获取到节点的数据内容
     *          threadFactory :
     *          executorService :
     *
     *  PathChildrenCache 定义了事件处理的回调接口PathChildrenCacheListener
     *          public void childEvent(CuratorFramework client, PathChildrenCacheEvent event)
     *          event type : child_add/child_update/child_removed
     */

    public static void main(String[] args) throws Exception {

        CuratorFramework build = CuratorFrameworkFactory.builder()
                .connectString(Constant.ZOOKEEPER_CONNECTION_ADDRESS)
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        build.start();

        PathChildrenCache pathChildrenCache = new PathChildrenCache(build, Constant.PATH, true);
        pathChildrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);

        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                switch (pathChildrenCacheEvent.getType()){
                    case CHILD_ADDED:
                    case CHILD_UPDATED:
                    case CHILD_REMOVED:
                        System.out.println( pathChildrenCacheEvent.getType() + " , " + pathChildrenCacheEvent.getData().getPath());
                    default:
                        break;
                }
            }
        });

        build.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(Constant.PATH);
        Thread.sleep(1000);
        build.create().withMode(CreateMode.EPHEMERAL).forPath(Constant.PATH + "/simons");
        Thread.sleep(1000);
        build.delete().forPath(Constant.PATH + "/simons");
        Thread.sleep(1000);
        build.delete().forPath(Constant.PATH);
        Thread.sleep(Integer.MAX_VALUE);
    }
}
