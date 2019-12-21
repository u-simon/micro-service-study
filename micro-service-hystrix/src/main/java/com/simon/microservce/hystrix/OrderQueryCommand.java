package com.simon.microservce.hystrix;

import com.netflix.hystrix.*;

/**
 * @author simon
 * @date 2019/12/19 15:42
 * @describe 一念花开, 一念花落
 */
public class OrderQueryCommand extends HystrixCommand<Integer> {

	private final int id;

	public OrderQueryCommand(int id) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("OrderService"))
				.andCommandKey(HystrixCommandKey.Factory.asKey("queryById"))
				.andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("QueryByIdThreadPoolKey"))
				.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
						// 至少有10个请求，熔断器才进行错误率计算
						.withCircuitBreakerRequestVolumeThreshold(10)
						// 熔断器中断请求5s后会进入半打开状态，放部分流量过去重试
						.withCircuitBreakerSleepWindowInMilliseconds(5000)
						// 错误率达到50开启熔断保护
						.withCircuitBreakerErrorThresholdPercentage(50)
						.withExecutionTimeoutEnabled(true)));
		this.id = id;
	}

	@Override
	protected Integer run() throws Exception {
		// 实际根据id查询订单
		return null;
	}

	@Override
	protected Integer getFallback() {
		return -1;
	}

    public static void main(String[] args) {
        System.out.println(new OrderQueryCommand(1).execute());
		System.out.println(new OrderQueryCommand(1).queue());
		System.out.println(new OrderQueryCommand(1).observe());
		System.out.println(new OrderQueryCommand(1).toObservable());

	}
}
