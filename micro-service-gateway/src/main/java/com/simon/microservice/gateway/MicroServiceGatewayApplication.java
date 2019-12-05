package com.simon.microservice.gateway;

import com.simon.microservice.gateway.factory.ElapsedGatewayFilterFactory;
import com.simon.microservice.gateway.filter.ElapsedFilter;
import com.simon.microservice.gateway.filter.RateLimitByCpuGatewayFilter;
import com.simon.microservice.gateway.filter.RateLimitByIpGatewayFilter;
import com.simon.microservice.gateway.filter.TokenFilter;
import com.simon.microservice.gateway.resolver.RemoteAddrKeyResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.time.Duration;

/**
 * @author simon
 */
// TODO Spring Cloud Gateway依赖Spring Boot和Spring Webflux提供的Netty runtime。它不能在传统的Servlet容器中工作或构建为WAR

@SpringBootApplication
public class MicroServiceGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroServiceGatewayApplication.class, args);
	}

	// @Bean
	public RouteLocator routeLocator(RouteLocatorBuilder builder) {
		return builder.routes().route(r -> r.path("/fluent/customer/**")
				// 过滤器
				.filters(f ->
				// stripPrefix 2 丢掉 路径的前两个 /fluent/customer/** -> /**
				f.stripPrefix(2)
						.filter(new RateLimitByIpGatewayFilter(10, 1, Duration.ofSeconds(1)))
						.filter(rateLimitByCpuGatewayFilter)
						// 添加响应头
						.addResponseHeader("X-Response-Default-Foo", "Default-Bar"))
				// 符合要求的则路由到这个服务上
				.uri("lb://consumer")
				// 顺序
				.order(0)
				// id
				.id("fluent_customer_service")).build();
	}

	@Autowired
	RateLimitByCpuGatewayFilter rateLimitByCpuGatewayFilter;

	@Bean
	public ElapsedGatewayFilterFactory elapsedGatewayFilterFactory() {
		return new ElapsedGatewayFilterFactory();
	}

	@Bean
	public TokenFilter tokenFilter() {
		return new TokenFilter();
	}

	@Bean
	public ElapsedFilter elapsedFilter() {
		return new ElapsedFilter();
	}

	@Bean(name = RemoteAddrKeyResolver.BEAN_NAME)
	public RemoteAddrKeyResolver remoteAddrKeyResolver() {
		return new RemoteAddrKeyResolver();
	}
}
