package com.simon.microservice.gateway.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 自定义过滤器 统计某个服务的响应时间
 * 
 * @author simon
 * @Date 2019/12/3 16:19
 * @Describe 一念花开, 一念花落
 */
public class ElapsedFilter implements GatewayFilter, Ordered {
	private static final Log log = LogFactory.getLog(GatewayFilter.class);
	private static final String ELAPSED_TIME_BEGIN = "elapsedTimeBegin";

	/**
	 * 用来实现你的自定义的逻辑的
	 *
	 * @param exchange
	 * @param chain
	 * @return
	 */
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		exchange.getAttributes().put(ELAPSED_TIME_BEGIN, System.currentTimeMillis());

		// chain.filter()方法之前的部分就是pre 之后的部分就是Post(then方法里面的部分)
		return chain.filter(exchange).then(Mono.fromRunnable(() -> {
			Long startTime = exchange.getAttribute(ELAPSED_TIME_BEGIN);
			if (startTime != null) {
				log.info(exchange.getRequest().getURI().getRawPath() + ":"
						+ (System.currentTimeMillis() - startTime) + " ms");
			}
		}));
	}

	/**
	 * 来给过滤器设定优先级别的，值越大则优先级越低
	 *
	 * @return
	 */
	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}
}
