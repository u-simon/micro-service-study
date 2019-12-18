package com.simon.microservce.hystrix.helloworld;

import rx.Observable;
import rx.Observer;
import rx.functions.Action1;

import java.util.concurrent.Future;

/**
 * @author simon
 * @date 2019/12/16 15:32
 * @describe 一念花开, 一念花落
 */
public class Test {

	public static void main(String[] args) throws Exception {
		HelloWorldCommand world = new HelloWorldCommand("World");

		// 同步执行
		String execute = world.execute();

		// 异步执行
		Future<String> queue = new HelloWorldCommand("World").queue();
		String future = queue.get();

		System.out.println(future.equals(execute));

		// Observable 如果只执行一次OnNext() 可以使用此方法获取返回值
		System.out.println(
				new HelloWorldCommandObserver("world").observe().toBlocking().toFuture().get());

		// reactive模式运行
		// observe() —— 返回一个 Hot Observable，这个命令将在调用 observe() 方法时被立即执行。你不用担心命令在返回 Observable
		// 时被执行而无法观察/订阅到结果，因为这个 Observable 内部在每次有新的 Subscriber 订阅时会重放 Observable 的行为。
		Observable<String> observable = new HelloWorldCommand("world").observe();
		// 非阻塞模式
		observable.subscribe(new Action1<String>() {
			@Override
			public void call(String s) {
				System.out.println(s);
			}
		});
		Observable<String> observe = new HelloWorldCommand("world").observe();
		observe.subscribe(new Observer<String>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onNext(String s) {
				System.out.println("observe OnNext : " + s);
			}
		});
		// toObservable() —— 返回一个 Cold Observable，调用完 toObservable()方法之后命令不会立即被执行，直到有 Subscriber
		// 订阅了这个 Observable
		Observable<String> observable1 = new HelloWorldCommand("world").toObservable();
		observable1.subscribe(new Observer<String>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onNext(String s) {
				System.out.println("toObservable OnNext : " + s);
			}
		});

		// 返回一个"Hot" Observable，这个方法内部会立即订阅底层 Observable，但你不用担心在这个方法返回 Observable
		// 再去订阅会不会丢数据，因为这个方法使用了
		// ReplaySubject 去订阅 Observable。即这个方法会让命令逻辑立即执行
		Observable<String> tom = new HelloWorldCommandObserver("Tom").observe();
		tom.subscribe(new Observer<String>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onNext(String s) {
				System.out.println("HelloWorldCommandObserver OnNext : " + s);
			}
		});
		// 返回一个"Cold" Observable，这个方法内部不会订阅底层的 Observable，即除非你去订阅返回的 Observable，否则命令逻辑不会执行
		Observable<String> jerry = new HelloWorldCommandObserver("jerry").toObservable();
		jerry.subscribe(new Observer<String>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onNext(String s) {
				System.out.println("HelloWorldCommandObserver toObservable : " + s);
			}
		});
		System.out.println(observable.toBlocking().toFuture().get());
	}
}
