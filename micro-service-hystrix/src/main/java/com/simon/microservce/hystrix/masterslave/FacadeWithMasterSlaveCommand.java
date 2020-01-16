package com.simon.microservce.hystrix.masterslave;

import static com.netflix.config.ConfigurationManager.getConfigInstance;

import com.netflix.config.DynamicBooleanProperty;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.hystrix.*;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

/**
 * @author simon
 * @date 2019/12/18 14:34
 * @describe 一念花开, 一念花落
 */
public class FacadeWithMasterSlaveCommand extends HystrixCommand<String> {

	private final int id;

	private final static DynamicBooleanProperty usePrimary =
			DynamicPropertyFactory.getInstance().getBooleanProperty("masterSlave.usePrimary", true);

    /**
     * Facade HystrixCommand 使用信号量进行隔离即可,因为其唯一的作用就是选择并执行主从命令中的一个命令
     * 而这两个命令均使用线程池进行隔离，没有必要为其引入多线程逻辑
     */
	public FacadeWithMasterSlaveCommand(int id) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("SystemX"))
				.andCommandKey(HystrixCommandKey.Factory.asKey("MasterSlaveCommand"))
				.andCommandPropertiesDefaults(
						HystrixCommandProperties.Setter().withExecutionIsolationStrategy(
								// 使用信号量隔离策略
								HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)));
		this.id = id;
	}

	@Override
	protected String run() throws Exception {
		if (usePrimary.get()) {
			return new MasterCommand(id).execute();
		}
		return new SlaveCommand(id).execute();
	}

	@Override
	protected String getFallback() {
		return String.valueOf(id);
	}

	private static class MasterCommand extends HystrixCommand<String> {
		private final int id;

		private MasterCommand(int id) {
			super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("SystemX"))
					.andCommandKey(HystrixCommandKey.Factory.asKey("MasterCommand"))
					.andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("MasterThreadPool"))
					.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
							// we default to a 600ms timeout for primary
							// 默认master超时时间为600ms
							.withExecutionTimeoutInMilliseconds(100)));
			this.id = id;
		}

		@Override
		protected String run() throws Exception {
			return "response from master " + id;
		}
	}


	private static class SlaveCommand extends HystrixCommand<String> {

		private final int id;

		public SlaveCommand(int id) {
			super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("SystemX"))
					.andCommandKey(HystrixCommandKey.Factory.asKey("slaveCommand"))
					.andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("SlaveThreadPool"))
					.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
							// we default to a 100ms timeout for secondary
							.withExecutionTimeoutInMilliseconds(100)));
			this.id = id;
		}


		@Override
		protected String run() throws Exception {
			return "response from slave " + id;
		}
	}

    public static void main(String[] args) {
        HystrixRequestContext hystrixRequestContext = HystrixRequestContext.initializeContext();
        getConfigInstance().setProperty("masterSlave.usePrimary", true);
        System.out.println(new FacadeWithMasterSlaveCommand(20).execute());
        getConfigInstance().setProperty("masterSlave.usePrimary", false);
        System.out.println(new FacadeWithMasterSlaveCommand(20).execute());
        hystrixRequestContext.shutdown();
    }
}
