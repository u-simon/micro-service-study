package com.simon.microservice.gateway.factory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * 自定义过滤工厂
 * 
 * @author simon
 * @Date 2019/12/3 16:38
 * @Describe 一念花开, 一念花落
 */
public class ElapsedGatewayFilterFactory
		extends AbstractGatewayFilterFactory<ElapsedGatewayFilterFactory.Config> {

	private static final Log log = LogFactory.getLog(GatewayFilter.class);
	private static final String ELAPSED_TIME_BEGIN = "elapsedTimeBegin";
	private static final String KEY = "withParams";


	@Override
	public List<String> shortcutFieldOrder() {
		return Arrays.asList(KEY);
	}

	public ElapsedGatewayFilterFactory() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		return new GatewayFilter() {
			@Override
			public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
				exchange.getAttributes().put(ELAPSED_TIME_BEGIN, System.currentTimeMillis());

				return chain.filter(exchange).then(Mono.fromRunnable(() -> {
					Long startTime = exchange.getAttribute(ELAPSED_TIME_BEGIN);
					if (startTime != null) {
						StringBuilder sb =
								new StringBuilder(exchange.getRequest().getURI().getRawPath())
										.append(":").append(System.currentTimeMillis() - startTime)
										.append("ms");
						if (config.isWithParams()) {
							sb.append(" params:").append(exchange.getRequest().getQueryParams());
						}
						log.info(sb.toString());
					}
				}));
			}
		};
	}

	public static class Config {

		private boolean withParams;

		public boolean isWithParams() {
			return withParams;
		}

		public void setWithParams(boolean withParams) {
			this.withParams = withParams;
		}

	}
}
