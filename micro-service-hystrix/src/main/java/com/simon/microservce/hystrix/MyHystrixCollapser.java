package com.simon.microservce.hystrix;

import com.netflix.hystrix.HystrixCollapser;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author fengyue
 * @Date 2019-06-24 17:18
 */
public class MyHystrixCollapser extends HystrixCollapser<List<String>, String, String> {

	private final String name;

	public MyHystrixCollapser(String name) {
		this.name = name;
	}

	@Override
	public String getRequestArgument() {
		return name;
	}

	@Override
    protected HystrixCommand<List<String>> createCommand(Collection<CollapsedRequest<String, String>> collapsedRequests) {
        return new BatchCommand(collapsedRequests);
    }

	@Override
	protected void mapResponseToRequests(List<String> batchResponse,
			Collection<CollapsedRequest<String, String>> collapsedRequests) {
	    int count = 0;

        for (CollapsedRequest<String, String> request : collapsedRequests) {
           request.setResponse(batchResponse.get(count++));
        }


	}

	private static final class BatchCommand extends HystrixCommand<List<String>> {

		private final Collection<CollapsedRequest<String, String>> requests;

		public BatchCommand(Collection<CollapsedRequest<String, String>> requests) {
			super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("exampleGroup"))
					.andCommandKey(HystrixCommandKey.Factory.asKey("getValueForKey")));
			this.requests = requests;
		}

		@Override
		protected List<String> run() throws Exception {
			System.out.println("真正执行请求");
			List<String> response = new ArrayList<>();
			for (CollapsedRequest<String, String> request : requests) {
				response.add("返回结果: " + request.getArgument());
			}
			return response;
		}
	}
}
