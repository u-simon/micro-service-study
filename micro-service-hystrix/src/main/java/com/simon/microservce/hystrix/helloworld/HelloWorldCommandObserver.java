package com.simon.microservce.hystrix.helloworld;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;
import rx.Observable;
import rx.Subscriber;

/**
 * @author simon
 * @Date 2019/12/16 15:13
 * @Describe 一念花开, 一念花落
 */
public class HelloWorldCommandObserver extends HystrixObservableCommand<String> {

	private final String name;

	public HelloWorldCommandObserver(String name) {
		super(HystrixCommandGroupKey.Factory.asKey("HelloWorld"));
		this.name = name;
	}

	@Override
	protected Observable<String> construct() {
		return Observable.create(new Observable.OnSubscribe<String>() {
			@Override
			public void call(Subscriber<? super String> subscriber) {
				if (!subscriber.isUnsubscribed()) {
					subscriber.onNext("hello");
//					subscriber.onNext(name + "!");
					subscriber.onCompleted();
				}
			}
		});
	}
}
