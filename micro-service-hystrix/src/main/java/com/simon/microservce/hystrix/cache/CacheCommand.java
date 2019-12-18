package com.simon.microservce.hystrix.cache;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

/**
 * @author simon
 * @date 2019/12/17 17:43
 * @describe 一念花开, 一念花落
 */
public class CacheCommand extends HystrixCommand<Boolean> {

	private final int value;

	public CacheCommand(int value) {
		super(HystrixCommandGroupKey.Factory.asKey("CacheGroup"));
		this.value = value;
	}

	@Override
	protected Boolean run() throws Exception {
		System.out.println(value);
		return value == 0 || value % 2 == 0;
	}

	/**
	 * 开启请求缓存
	 * 
	 * @return
	 */
	@Override
	protected String getCacheKey() {
		return String.valueOf(value);
	}

	public static void main(String[] args) {
		HystrixRequestContext hystrixRequestContext = HystrixRequestContext.initializeContext();
		Boolean execute = new CacheCommand(2).execute();
        CacheCommand cacheCommand = new CacheCommand(2);
        cacheCommand.execute();
        System.out.println(cacheCommand.isResponseFromCache());
        hystrixRequestContext.shutdown();
		// run方法的print只执行了一次

	}
}
