package com.simon.microservce.hystrix.rxjava;

import io.reactivex.*;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.schedulers.SingleScheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * @author simon
 * @date 2019/12/20 19:10
 * @describe 一念花开, 一念花落
 */
public class ObservableTest {

	public static void main(String[] args) {

		// 异步+连式编程
		Observable.create(new ObservableOnSubscribe<String>() {
			@Override
			public void subscribe(ObservableEmitter<String> emitter) throws Exception {
				// onNext 方法可以无线调用
				emitter.onNext("连载1");
				emitter.onNext("连载2");
				emitter.onNext("连载3");
				// onError 和 onComplete 是互斥的, onComplete 可以重复调用,但是只会被
				// 观察者接收一次, onError不可以重复调用,第二次调用就会报异常
				emitter.onComplete();
			}
		}).observeOn(new SingleScheduler()).subscribeOn(Schedulers.io())
				/**
				 * 这里是小说订阅读者 -> 之所以这样是因为RxJava主要是想保持自己的链式编程,不得不把Observable(被观察者)放在前面
				 */
				.subscribe(new Observer<String>() {
					Disposable disposable;

					// 观察者 --> 读者
					@Override
					public void onSubscribe(Disposable disposable) {
						System.out.println("onSubscribe");
						this.disposable = disposable;
					}

					// onNext onError onComplete 和被观察者发射的方法一一对应,这里相当于接收

					@Override
					public void onNext(String s) {
						System.out.println(s);
						// 这里是取消订阅了
						// disposable.dispose();
					}

					@Override
					public void onError(Throwable throwable) {
						System.out.println("onError");
					}

					@Override
					public void onComplete() {
						System.out.println("onComplete");
					}
				});
	}
}
