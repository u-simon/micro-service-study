package com.simon.microservice.microservice.zookeeper.client.zkclient;

import com.simon.microservice.microservice.zookeeper.api.Constant;
import org.I0Itec.zkclient.ZkClient;

/**
 * @author simon
 * @Date 2019/10/26 14:19
 * @Describe
 */
public class CreateSession {

    /**
     * public ZkClient(String servers)
     * public ZkClient(String zkServers, int connectionTimeout)
     * public ZkClient(String zkServers, int sessionTimeout, int connectionTimeout)
     * public ZkClient(String zkServers, int sessionTimeout, int connectionTimeout, ZkSerializer zkSerializer)
     * public ZkClient(IZkConnection connection)
     * public ZkClient(IZkConnection connection, int connectionTimeout)
     * public ZkClient(IZkConnection connection, int connectionTimeout, ZkSerializer zkSerializer)
     *
     *
     * parameters declare
     *      zkServers : 指Zookeeper服务器列表 host:port
     *      sessionTimeout : 会话超时时间,单位ms, 默认是30000ms
     *      connectionTimeout :  连接创建超时时间,单位ms, 此参数表明如果在这个时间段内无法和Zookeeper建立连接,那么就放弃连接,直接抛出异常
     *      connection : IZkConnection接口的实现类
     *      zkSerializer : 自定义序列化器
     */

    public static void main(String[] args) {
        ZkClient zkClient = new ZkClient(Constant.ZOOKEEPER_CONNECTION_ADDRESS, 5000);
        System.out.println("Zookeeper session established");
    }
}
