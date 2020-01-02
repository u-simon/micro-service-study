package com.simon.microservce.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;

/**
 * @author fengyue
 * @Date 2019-06-24 16:40
 */
public class MyHystrixCommand extends HystrixCommand<String> {

	private final String name;

	public MyHystrixCommand(String name) {
//		super(HystrixCommandGroupKey.Factory.asKey("MyGroup"));

		//Semaphore
//		super(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("MyGroup"))
//				.andCommandPropertiesDefaults(
//						HystrixCommandProperties.Setter().withExecutionIsolationStrategy(
//								HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)));

		//Thread
		super(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("MyGroup"))
				.andCommandPropertiesDefaults(
						HystrixCommandProperties.Setter().withExecutionIsolationStrategy(
								HystrixCommandProperties.ExecutionIsolationStrategy.THREAD))
				.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
						.withCoreSize(10).withMaxQueueSize(100).withMaximumSize(100)));
		this.name = name;
	}

	@Override
	protected String run() throws Exception {
		// Thread.sleep(1000000);
		System.out.println("get data");
		return this.name + " : " + Thread.currentThread().getName();
	}

	@Override
	protected String getFallback() {
		return "failure";
	}

	@Override
	protected String getCacheKey() {
		return String.valueOf(this.name);
	}
}
