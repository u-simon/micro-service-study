package com.simon.microservice.consumer;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.ribbon.RibbonClient;

import javax.annotation.Resource;
import java.util.List;

@SpringBootApplication
@EnableEurekaClient
@EnableHystrix
public class MicroServiceConsumerApplication {
	public static void main(String[] args) {
		SpringApplication.run(MicroServiceConsumerApplication.class, args);
	}

	@Resource(name = "eurekaClient")
	EurekaClient eurekaClient;

	public String serviceUrl() {
		InstanceInfo stores = eurekaClient.getNextServerFromEureka("STORES", false);
		return stores.getHomePageUrl();
	}


	DiscoveryClient discoveryClient;

	public String serviceUrl1(){
		List<InstanceInfo> stores = discoveryClient.getInstancesById("stores");
		if (stores != null && stores.size() > 0){
			return stores.get(0).getIPAddr();
		}
		return null;
	}
}
