package com.simon.microservce.hystrix.usagemode;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

/**
 * @author simon
 * @date 2019/12/18 10:30
 * @describe 一念花开, 一念花落
 */
public class FailSilentlyCommand extends HystrixCommand<String> {

	/**
	 * 静默失败
     *   -> 静默失败等价于返回结果或功能被移除，通过返回 null 空Map,空List或其他类似结果来实现
     *   -> 可以实现getFallback()方法来实现静默失败
	 */
	private final boolean flag;

	public FailSilentlyCommand(boolean flag) {
		super(HystrixCommandGroupKey.Factory.asKey("FailSilentlyGroup"));
		this.flag = flag;
	}

	@Override
	protected String run() throws Exception {
		if (flag) {
			throw new RuntimeException("failure from FailSilentlyCommand");
		}
		return "SUCCESS";
	}

	@Override
	protected String getFallback() {
		return null;
	}

    public static void main(String[] args) {
        System.out.println( "result : " + new FailSilentlyCommand(true).execute());
    }
}
