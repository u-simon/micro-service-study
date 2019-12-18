package com.simon.microservce.hystrix.fallback;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;

/**
 * @author simon
 * @Date 2019/12/16 16:05
 * @Describe 一念花开, 一念花落
 */
public class FailureCommand extends HystrixCommand<String> {

	private final String name;


	private static final Setter cacheSetter =
			Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("FailureGroup"))
					.andCommandKey(HystrixCommandKey.Factory.asKey("failure"));

	public FailureCommand(String name) {
		// super(HystrixCommandGroupKey.Factory.asKey("failure"));
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("FailureGroup"))
				.andCommandKey(HystrixCommandKey.Factory.asKey("failure")));
		this.name = name;
	}

	/**
	 * run() 方法中抛出的所有异常，除开 HystrixBadRequestException 之外，均会被算作命令执行失败而触发 getFallback() 方法的调用和熔断器逻辑
	 * 你可以通过将异常放进 HystrixBadRequestException 并通过 getCause()
	 * 获取真实的异常，而不会触发失败回退。HystrixBadRequestException
	 * 专门用来处理这种不应该算作命令执行失败，并且不应该触发熔断器的场景，例如向调用者报告参数不合法或者非系统异常等
	 */
	@Override
	protected String run() throws Exception {
		throw new RuntimeException("this command always fails");
	}

	/**
	 * 通过提供一个失败回退的方法(fallback方法), hystrix在主命令逻辑发生异常时能从这个方法中得到一个默认值或者一些数据作为命令的返回值
	 * ,从而实现优雅的服务降级,当然,你可能会给所有可能失败的命令增加失败回退方法,但也有以下例外 1.写操作
	 * 如果一个Hystrix命令用于写操作而不是返回一个值(例如一个返回void的HystrixCommand或者一个一个返回空Observable的HystrixObservableCommand)
	 * 失败回退逻辑并没有多大意义,一般写操作如果失败,应该向上传播至命令的调用者 2.批处理/离线计算 如果你编写的 Hystrix
	 * 命令用于填充缓存/生成报表/或者做一些离线计算，通常将错误向上传播到调用者以便其可以稍后重试会更加合适, 向上传递一个降级后的结果对调用者来说没有意义
	 */

	@Override
	protected String getFallback() {
		return "hello Failure " + name + " !";
	}
}
