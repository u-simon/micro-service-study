package com.simon.microservce.hystrix.rxjava;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.*;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author simon
 * @date 2019/12/21 11:26
 * @describe 一念花开, 一念花落
 */
public class RxJavaDemo {
	/**
	 * Rx: reactive X 官方定义是基于"观察者模式",实现基于事件流异步处理的lib,适用于实现响应式编程的框架
	 * 	-> 特点:
	 * 		①链式调用,异步处理复杂问题
	 * 		②使用推的方式,有数据主动推送给消费者
	 * 	->主要类:
	 * 		①被观察者 Observable
	 * 			a.注册观察者 subscribe()
	 * 			b.事件回调
	 * 			c.增加如下方法:
	 *			 onComplete()用于生产者没有更多数据可用时发出通知
	 *			 onError() 生产者发生错误时能够发出通知
	 * 		②观察者 Observer
	 * 			a.事件消费 consumer()
	 *
	 * 	Observable(被观察者)常用操作
	 * 		①创建
	 * 			a. Observable.from() 遍历集合或数组,逐个推送其中的数据
	 * 			b. Observable.just() 推送一个普通的Java函数的返回值:非常有用,我们可以将远程调用的结果推送到处理类中
	 * 			c. Observable.just() 可以接受多个参数进行推送
	 * 		 	d. Observable.timer()/Observable.interval() 定时发送,可以用于代替定时任务
	 * 		②预处理
	 * 			map() 在"观察者"消费前,将处理逻辑作用于每个推送的数据
	 * 	    ③过滤
	 * 	    	filter() 从推送的数据中过滤出想要的进行处理
	 * 	    	distinct() 去重,用于推送是出错引起的重复推送的情况(推送的数据一般实现Comparable接口)
	 * 	    	distinctUnitChange() 观察者可能一直有数据产生,这里要求直到有数据变化,观察者才能接收到
	 * 	    ④组合: 同时处理多个来源的时间
	 * 	    	marge() 将多个Observable的数据合并到一起处理
	 * 	    	concat() 顺序执行,特别适合使用队列的情况
	 */

	public static void main(String[] args) throws IOException {
		Observer<String> observer = new Observer<String>() {
			@Override
			public void onSubscribe(Disposable disposable) {
				System.out.println("onSubscribe");
			}

			@Override
			public void onNext(String s) {// 接收事件处理
				System.out.println(s);
			}

			@Override
			public void onError(Throwable throwable) {// 事件产生过程发生error时
				System.out.println("onError");
			}

			@Override
			public void onComplete() {// 收到onComplete通知时
				System.out.println("onComplete");
				System.out.println();
			}
		};

		Observable.just("123", "345").mergeWith(CompletableObserver::onComplete).subscribe(observer);

		Observable.just("123").distinctUntilChanged().subscribe(observer);

		Observable.just("simon", "simon").distinct().subscribe(observer);

		Observable.just("123", "335").filter(value-> !value.equals("123")).subscribe(observer);

		Observable.just("123").map(new Function<String, String>() {
			@Override
			public String apply(String s) throws Exception {
				return "456";
			}
		}).subscribe(observer);

		Observable.fromArray("123", "456").subscribe(observer);

		Observable.just("simon", "lemon").subscribe(observer);

		Observable.create(new ObservableOnSubscribe<String>() {
			@Override
			public void subscribe(ObservableEmitter<String> emitter) throws Exception {
				emitter.onNext("event 1");
				emitter.onNext("event 2");
				emitter.onNext("event 3");
				emitter.onComplete();
			}
		}).subscribe(observer);

		Observable.timer(1, TimeUnit.SECONDS).subscribe(new Observer<Long>() {
			@Override
			public void onSubscribe(Disposable d) {

			}

			@Override
			public void onNext(Long aLong) {
				System.out.println(aLong);
			}

			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onComplete() {
				System.out.println("onComplete");
			}
		});

		Observable.just("hello world")
				//异步
				.subscribeOn(Schedulers.newThread())
				.subscribe(observer);

		Observable.concat(Observable.just("123"), Observable.just("456"), Observable.just("789"))
				.subscribeOn(Schedulers.newThread())
				.subscribe(observer);
	}
}
