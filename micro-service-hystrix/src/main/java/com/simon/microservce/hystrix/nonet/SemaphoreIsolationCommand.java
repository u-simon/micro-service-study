package com.simon.microservce.hystrix.nonet;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;

/**
 * @author simon
 * @date 2019/12/18 15:02
 * @describe 一念花开, 一念花落
 */
public class SemaphoreIsolationCommand extends HystrixCommand<String> {

	/**
	 * 如果你的命令不需要访问网络,但需要考虑延迟或线程池占满等情况,你可以将executionIsolationStrategy
	 */

	private final int id;

	public SemaphoreIsolationCommand(int id) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"))
				// 我们只做内存内的缓存查询,因此我们将隔离策略设置为信号量 SEMAPHORE
				.andCommandPropertiesDefaults(
						HystrixCommandProperties.Setter().withExecutionIsolationStrategy(
								HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)));
		this.id = id;
	}

	@Override
	protected String run() throws Exception {
		// 真是环境肯能会从内存数据接口中获取数据
		return "value from map " + id;
	}
}
