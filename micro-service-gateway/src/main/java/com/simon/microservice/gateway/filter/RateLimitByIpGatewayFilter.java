package com.simon.microservice.gateway.filter;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import reactor.core.publisher.Mono;

/**
 * 基于ip的限流--令牌桶
 * 
 * @author simon
 * @Date 2019/12/3 17:21
 * @Describe 一念花开, 一念花落
 */

@CommonsLog
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RateLimitByIpGatewayFilter implements GatewayFilter, Ordered {

	/**
	 * 桶的最大容量,即能装在Token的最大数量
	 */
	int capacity;
	/**
	 * 每次Token的补充量
	 */
	int refillTokens;

	/**
	 * 补充Token的时间间隔
	 */
	Duration refillDuration;

	private static final Map<String, Bucket> CACHE = new ConcurrentHashMap<>();

	private Bucket createNewBucket() {
		Bandwidth limit = Bandwidth.classic(capacity, Refill.of(refillTokens, refillDuration));
		return Bucket4j.builder().addLimit(limit).build();
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		String ip = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
		Bucket bucket = CACHE.computeIfAbsent(ip, k -> createNewBucket());
		log.debug("IP: " + ip + "，TokenBucket Available Tokens: " + bucket.getAvailableTokens());
		if (bucket.tryConsume(1)) {
			return chain.filter(exchange);
		} else {
			exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
			return exchange.getResponse().setComplete();
		}
	}

	@Override
	public int getOrder() {
		return -100;
	}
}
