package com.simon.microservice.gateway.resolver;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author simon
 * @Date 2019/12/4 10:30
 * @Describe 一念花开, 一念花落
 */
public class RemoteAddrKeyResolver implements KeyResolver {
    public static final String BEAN_NAME = "remoteAddrKeyResolver";
    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        return Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
    }
}
