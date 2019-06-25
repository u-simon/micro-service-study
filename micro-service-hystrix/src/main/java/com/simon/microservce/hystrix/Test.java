package com.simon.microservce.hystrix;

import com.netflix.hystrix.HystrixRequestCache;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

import java.util.concurrent.Future;

/**
 * @author fengyue
 * @Date 2019-06-24 16:43
 */
public class Test {

	public static void main(String[] args) throws Exception {
		HystrixRequestContext initializeContext = HystrixRequestContext.initializeContext();
		// //同步调用
		// String result = new MyHystrixCommand("simon").execute();
		// System.out.println(result);
		//
		// //异步调用
		// Future<String> future = new MyHystrixCommand("simon").queue();
		// System.out.println(future.get());

		String result = new ClearCacheHystrixCommand("simon").execute();
		System.out.println(result);
		ClearCacheHystrixCommand.flushCache("simon");

		Future<String> future = new ClearCacheHystrixCommand("simon").queue();
		System.out.println(future.get());



        Future<String> future1 = new MyHystrixCollapser("simon").queue();
        Future<String> future2 = new MyHystrixCollapser("simon").queue();
        System.out.println(future1.get() + " ----- " + future2.get());

        initializeContext.shutdown();
    }
}

