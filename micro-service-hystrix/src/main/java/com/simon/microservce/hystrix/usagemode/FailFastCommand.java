package com.simon.microservce.hystrix.usagemode;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

/**
 * @author simon
 * @date 2019/12/18 10:17
 * @describe 一念花开, 一念花落
 */
public class FailFastCommand extends HystrixCommand<String> {
	/**
	 * 快速失败
     *    -> 一种最基本的用法是执行一个命令,并且没有定义失败回退逻辑,如果有任何错误发生，这个命令将会抛出异常
	 */

	public final boolean throwException;

	public FailFastCommand(Boolean throwException) {
		super(HystrixCommandGroupKey.Factory.asKey("FailFastGroup"));
		this.throwException = throwException;
	}

	@Override
	protected String run() throws Exception {
		if (throwException) {
			throw new RuntimeException("failure from FailFastCommand");
		}
		return "SUCCESS";
	}

	public static void main(String[] args) {
		String execute = new FailFastCommand(true).execute();
	}
}
