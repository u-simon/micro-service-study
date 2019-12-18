package com.simon.microservce.hystrix.fallback;

import rx.Observable;
import rx.Observer;

/**
 * @author simon
 * @Date 2019/12/16 16:18
 * @Describe 一念花开, 一念花落
 */
public class Test {

	public static void main(String[] args) {
		FailureCommand world = new FailureCommand("World");
		System.out.println(world.execute());

		Observable<String> tom = new FailureObservableCommand("Tom").observe();
		tom.subscribe(new Observer<String>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onNext(String s) {
				System.out.println(s);
			}
		});
	}
}
