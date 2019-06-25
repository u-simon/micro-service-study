package com.simon.microservice.ribbon.controller;


import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.LoadBalancerBuilder;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.reactive.LoadBalancerCommand;
import com.netflix.loadbalancer.reactive.ServerOperation;
import rx.Observable;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fengyue
 * @Date 2019-06-20 11:17
 */
public class RibbonController {
    public static void main(String[] args) {
        List<Server> serverList = new ArrayList<>();
        serverList.add(new Server("localhost", 8001));
        serverList.add(new Server("localhost", 8003));

        BaseLoadBalancer baseLoadBalancer = LoadBalancerBuilder.<Server>newBuilder().buildFixedServerListLoadBalancer(serverList);

        for (int i = 0; i < 5; i++) {
            String first = LoadBalancerCommand.<String>builder().withLoadBalancer(baseLoadBalancer).build().submit(new ServerOperation<String>() {
                @Override
                public Observable<String> call(Server server) {
                    try {
                        String addr = "http://" + server.getHost() + ":" + server.getPort() + "/provider/hello";
                        System.out.println("url " + addr);
                        URL url = new URL(addr);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.connect();
                        InputStream inputStream = connection.getInputStream();
                        byte[] data = new byte[8096];
                        inputStream.read(data);
                        return Observable.just(new String(data,0, 5));
                    } catch (Exception e) {
                        return Observable.error(e);
                    }
                }
            }).toBlocking().first();
            System.out.println("result = " + first);
        }
    }
}
