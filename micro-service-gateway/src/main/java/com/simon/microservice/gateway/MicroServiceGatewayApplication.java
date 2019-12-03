package com.simon.microservice.gateway;

import com.simon.microservice.gateway.factory.ElapsedGatewayFilterFactory;
import com.simon.microservice.gateway.filter.ElapsedFilter;
import com.simon.microservice.gateway.filter.TokenFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

/**
 * @author simon
 */
@SpringBootApplication
public class MicroServiceGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroServiceGatewayApplication.class, args);
	}

//	@Bean
	public RouteLocator routeLocator(RouteLocatorBuilder builder) {
		return builder.routes().route(r -> r.path("/fluent/customer/**")
				// 过滤器
				.filters(f ->
				// stripPrefix 2 丢掉 路径的前两个 /fluent/customer/** -> /**
				f.stripPrefix(2)
						// 添加响应头
						.addResponseHeader("X-Response-Default-Foo", "Default-Bar"))
				// 符合要求的则路由到这个服务上
				.uri("lb://consumer")
				// 顺序
				.order(0)
				// id
				.id("fluent_customer_service")).build();
	}

	@Bean
	public ElapsedGatewayFilterFactory elapsedGatewayFilterFactory() {
		return new ElapsedGatewayFilterFactory();
	}

	@Bean
	public TokenFilter tokenFilter(){
		return new TokenFilter();
	}

	@Bean
	public ElapsedFilter elapsedFilter(){
		return new ElapsedFilter();
	}
}
