package com.simon.microservce.hystrix.mergerequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

import com.netflix.hystrix.HystrixCollapser;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

/**
 * @author simon
 * @date 2019/12/17 19:26
 * @describe 一念花开, 一念花落
 *
 *           BatchReturnType, ResponseType, RequestArgumentType
 */
public class CommandCollapser extends HystrixCollapser<List<String>, String, Integer> {
	/**
	 * 通过合并请求,可以将多个请求合并到单个HystrixCommand的执行周期中 合并器(collapser)可以使用批大小(batch size)和自创建批命令以来耗时(elapsed
	 * time)来触发请求合并
	 *
	 * Hystrix支持两种风格的请求合并：请求范围合并/全局范围合并，默认情况下使用请求范围合并，也可以通过合并器的构造来配置
	 *
	 * 请求风格的合并器在单个HystrixRequestContext中收集请求，而全局范围风格的合并器则从多个HystrixRequestContext中收集请求，
	 * 因此,如果下游依赖无法在一次命令执行中处理多个HystrixRequestContext,选择请求范围风格的合并器比价合适
	 */


	private final Integer key;

	public CommandCollapser(Integer key) {
		this.key = key;
	}

	@Override
	public Integer getRequestArgument() {
		return key;
	}

	@Override
	protected HystrixCommand<List<String>> createCommand(
			Collection<CollapsedRequest<String, Integer>> collapsedRequests) {
		return new BatchCommand(collapsedRequests);
	}

	@Override
	protected void mapResponseToRequests(List<String> batchResponse,
			Collection<CollapsedRequest<String, Integer>> collapsedRequests) {
		int count = 0;
		for (CollapsedRequest<String, Integer> collapsedRequest : collapsedRequests) {
			collapsedRequest.setResponse(batchResponse.get(count++));
		}
	}


	private static final class BatchCommand extends HystrixCommand<List<String>> {

		private final Collection<CollapsedRequest<String, Integer>> requests;

		public BatchCommand(Collection<CollapsedRequest<String, Integer>> requests) {
			super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("CollapseGroup"))
					.andCommandKey(HystrixCommandKey.Factory.asKey("GetValueForKey")));
			this.requests = requests;
		}

		@Override
		protected List<String> run() throws Exception {
			System.out.println("requests");
			List<String> response = new ArrayList<>();
			for (CollapsedRequest<String, Integer> request : requests) {
				response.add("ValueForKey" + request.getArgument());
			}
			return response;
		}
	}

	public static void main(String[] args) throws Exception {
        /**
         * 要使用请求范围的特性(请求缓存，请求合并，请求日志) 必须手动关系HystrixRequestContext的生命周期
         */
		HystrixRequestContext hystrixRequestContext = HystrixRequestContext.initializeContext();
		new CommandCollapser(1).queue().get();
		new CommandCollapser(2).queue().get();
		new CommandCollapser(3).queue().get();
		new CommandCollapser(4).queue().get();

		/**
		 * 请求结束后关闭
		 */
		hystrixRequestContext.shutdown();
	}

}
