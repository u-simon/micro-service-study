package com.simon.microservce.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixRequestCache;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;

/**
 * @author fengyue
 * @Date 2019-06-24 17:07
 */
public class ClearCacheHystrixCommand extends HystrixCommand<String> {

	private final String name;

	private static final HystrixCommandKey GETTER_KEY = HystrixCommandKey.Factory.asKey("MyKey");

	public ClearCacheHystrixCommand(String name) {
		super(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("MyGroup")).andCommandKey(GETTER_KEY));
		this.name = name;
	}

	@Override
	protected String run() throws Exception {
        System.out.println("get data");
        return this.name + " : " + Thread.currentThread().getName();
	}

    @Override
    protected String getCacheKey() {
        return String.valueOf(this.name);
    }

    @Override
    protected String getFallback() {
        return "失败了";
    }

    public static void flushCache(String name){
        HystrixRequestCache.getInstance(GETTER_KEY, HystrixConcurrencyStrategyDefault.getInstance()).clear(name);
    }
}
