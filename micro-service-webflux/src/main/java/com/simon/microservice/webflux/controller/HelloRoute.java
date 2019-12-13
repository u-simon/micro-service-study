package com.simon.microservice.webflux.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * @author simon
 * @Date 2019/12/12 10:29
 * @Describe 一念花开, 一念花落
 */
@Configuration
public class HelloRoute {

	@Bean
	public RouterFunction<ServerResponse> routeHello(HelloHandler helloHandler) {
	    //将/hello请求bind到 HelloHandler的 hello方法上
		return RouterFunctions.route(
				RequestPredicates.GET("/hello").and(RequestPredicates.accept(MediaType.TEXT_PLAIN)),
				helloHandler::hello);
	}
}
