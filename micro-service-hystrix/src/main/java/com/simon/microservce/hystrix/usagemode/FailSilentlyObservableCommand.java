package com.simon.microservce.hystrix.usagemode;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;
import rx.Observable;

import java.util.concurrent.ExecutionException;

/**
 * @author simon
 * @date 2019/12/18 11:15
 * @describe 一念花开, 一念花落
 */
public class FailSilentlyObservableCommand extends HystrixObservableCommand<String> {

	private final boolean flag;

	public FailSilentlyObservableCommand(boolean flag) {
		super(HystrixCommandGroupKey.Factory.asKey("FailSilentlyObservableGroup"));
		this.flag = flag;
	}

	@Override
	protected Observable<String> construct() {
	    if (!flag){
	        return  Observable.just("11");
        }
		return null;
	}

	@Override
	protected Observable<String> resumeWithFallback() {
		return Observable.empty();
	}

	public static void main(String[] args) throws ExecutionException, InterruptedException {
		System.out.println(new FailSilentlyCommand(false).observe().toBlocking().toFuture().get());
	}
}
