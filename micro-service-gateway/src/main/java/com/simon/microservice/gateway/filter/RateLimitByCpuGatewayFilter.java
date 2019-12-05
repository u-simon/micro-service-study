package com.simon.microservice.gateway.filter;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * 基于系统负载的动态限流
 * 
 * @author simon
 * @Date 2019/12/4 10:44
 * @Describe 一念花开, 一念花落
 */
@CommonsLog
@Component
public class RateLimitByCpuGatewayFilter implements GatewayFilter, Ordered {

	@Autowired
	MetricsEndpoint metricsEndpoint;

	private static final String METRIC_NAME = "system.cpu.usage";
	private static final double MAX_USAGE = 0.50D;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		Double systemCpuUsage = metricsEndpoint.metric(METRIC_NAME, null).getMeasurements().stream()
				.filter(Objects::nonNull).findFirst().map(MetricsEndpoint.Sample::getValue)
				.filter(Double::isFinite).orElse(0.0D);
		boolean ok = systemCpuUsage < MAX_USAGE;
		log.debug("system.cpu.usage: " + systemCpuUsage + " ok: " + ok);
		if (!ok) {
			exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
			return exchange.getResponse().setComplete();
		}
		return chain.filter(exchange);
	}

	@Override
	public int getOrder() {
		return 0;
	}
}
