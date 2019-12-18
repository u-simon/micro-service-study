package com.simon.microservce.hystrix.helloworld;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

/**
 * @author simon
 * @Date 2019/12/16 15:06
 * @Describe 一念花开, 一念花落
 */
public class HelloWorldCommand extends HystrixCommand<String> {
	private final String name;

	public HelloWorldCommand(String name) {
		super(HystrixCommandGroupKey.Factory.asKey("HelloWorld"));
		this.name = name;
	}

	@Override
	protected String run() throws Exception {
		return "Hello" + name + '!';
	}
}
