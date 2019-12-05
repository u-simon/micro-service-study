package com.simon.microservice.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author simon
 * @Date 2019/12/4 10:55
 * @Describe 一念花开, 一念花落
 */
@RestController
public class FallbackController {

	@GetMapping("/fallback")
	public String fallback() {
		return "Hello world ! \n from gateway";
	}
}
