package com.simon.microservce.hystrix.rxjava;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.*;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.schedulers.SingleScheduler;
import io.reactivex.schedulers.Schedulers;
import lombok.Data;

/**
 * @author simon
 * @date 2019/12/20 19:10
 * @describe 一念花开, 一念花落
 */
public class ObservableTest {

	public static void main(String[] args) throws Exception {
		System.out.println(new Date(1577433461000L));
		System.out.println("-------defer-------------");
		/**
		 * Observable 操作符
		 * 	-> defer 操作符
		 * 		Observable 用于封装数据, defer提供懒绑定, 直到有订阅时才创建Observable,即冷Observable
		 * 	注:
		 * 		a.冷热问题
		 * 			hot Observable：创建后马上执行, 生成可观察事件,但观察者注册完成后,可能从中间消费事件,造成一定量事件无法观察到
		 * 			cold Observable：一直等待, 直到有观察者订阅它才开始发送数据, 因此观察者可以确保收到整个数据徐柳
		 * 		b.使用时通过回调传入一个已有Observable, defer() 进行包装
		 */
		System.out.println("do hot observable");
		Content hot = new Content();
		hot.setData("before");
		Observable<String> just = Observable.just(hot.data);
		hot.setData("after");
		/**
		 * 这里输出"before" 因为通过just创建时已经绑定 "before"的引用, 用于保持数据 "原生性" 很有用
		 *
		 * 生成Observable时已经绑定了"before",所以即使修改了content.data的值,对观察者是不可见的
		 */
		just.subscribe(System.out::println);

		System.out.println("do cold observable");
		Content cold = new Content();
		cold.setData("before");
		Observable<Object> defer = Observable.defer(new Callable<ObservableSource<String>>() {
			@Override
			public ObservableSource<String> call() throws Exception {
				return Observable.just(cold.data);
			}
		});
		cold.setData("after");
		/**
		 * 这里输出"after", 延迟绑定,直到subscribe()调用时,才调用获取Content.data, 用于保证数据 "最新性" 很有用
		 *
		 * 延迟绑定, 修改content.data的值 观察者看到的就是修改之后的值,对于订阅到最新数据非常有用
		 */
		defer.subscribe(System.out::println);

		System.out.println("---------lift-----------");

		/**
		 * lift操作符
		 * 	-> 方法负责将一个Observable转换成另一个Observable,转换方式通过Operator提供
		 * 	注:
		 * 		lift()方法是在一个已有Observable上调用,可以用于Observable封装数据变换
		 */

		Observable<String> observable = Observable.just("1");

		Observable<Integer> lift = observable.lift(new ObservableOperator<Integer, String>() {
			@Override
			public Observer<? super String> apply(Observer<? super Integer> observer) throws Exception {
				return new Observer<String>() {
					@Override
					public void onSubscribe(Disposable d) {
						observer.onSubscribe(d);
					}

					@Override
					public void onNext(String s) {
						observer.onNext(Integer.parseInt(s));
					}

					@Override
					public void onError(Throwable e) {
						observer.onError(e);
					}

					@Override
					public void onComplete() {
						observer.onComplete();
					}
				};
			}
		});
		lift.subscribe(System.out::println);

		System.out.println("---------dispose-----------");

		/**
		 * Disposable
		 * 	-> 用于主动解除订阅,核心方法 dispose()
		 * 	   注：
		 * 	   	如果任务异步执行且阻塞时, 调用dispose()会中断阻塞
		 * 	   	如果任务正在执行非阻塞状态,则不会中断,会一直执行完,退出
		 */

		Observable<String> deferOb = Observable.create(new ObservableOnSubscribe<String>() {
			@Override
			public void subscribe(ObservableEmitter<String> emitter) throws Exception {
				emitter.onNext("simon");
				emitter.onNext("lemon");
				emitter.onNext("amy");
				emitter.onComplete();
			}
		});
		Observable<Object> disposableOb = Observable.defer(new Callable<ObservableSource<?>>() {
			@Override
			public ObservableSource<?> call() throws Exception {
				return deferOb;
			}
		});
		Disposable disposable = disposableOb.observeOn(Schedulers.newThread())
				.subscribeOn(Schedulers.newThread())
				.subscribe(result -> {
					System.out.println(result);
				}, e -> {
					e.printStackTrace();
				});

		TimeUnit.MICROSECONDS.sleep(50);
		disposable.dispose();

		System.out.println("-------compositeDisposable-------------");

		/**
		 * 用于添加一组Disposable,快速解除订阅,清理资源时很有用
		 * -> 核心方法
		 * 		add() 添加到组中
		 * 		clear() 快速解除所有订阅,中断所有添加的"被观察者"正在执行的任务
		 */

		CompositeDisposable compositeDisposable = new CompositeDisposable();

		Observable.just("message").subscribe(new Observer<String>() {
			@Override
			public void onSubscribe(Disposable d) {
				// 添加待管理的订阅
				compositeDisposable.add(d);
			}

			@Override
			public void onNext(String s) {
				System.out.println(s);
			}

			@Override
			public void onError(Throwable e) {
				e.printStackTrace();
			}

			@Override
			public void onComplete() {
				System.out.println("onComplete");
			}
		});

		compositeDisposable.clear();


		System.out.println("-------scheduler-------------");
		/**
		 * 线程调度 scheduler
		 *  -> 异步执行任务代码
		 *		observable.observeOn(Schedulers.newThread()).subscribeOn(Schedulers.newThread())
		 *	RxJava实现异步的主要结构,核心三大
		 *		1.Scheduler: 调度器,用于调度一个Worker执行任务,核心方法： Worker createWorker();
		 *		2.Scheduler.Worker: 执行线程任务, 核心方法: schedule(@NotNull Runnable run)
		 *		3.Schedulers: 静态工厂, 返回Scheduler实例,2.x主要有如下几种:
		 *			a.trampoline：当前线程单线程执行,执行先进先出,一般用于测试
		 *			b.single:共享单线程异步任务,任务先出执行
		 *			c.new_thread：每一个任务一个新的后台线程执行的调度器实例
		 *			d.computation:用于执行计算型任务,使用固定大小的线程池,线程数=cpu核数
		 *			e.io: 用于执行IO密集型任务,使用大小动态变化的线程池
		 */
		Scheduler trampoline = Schedulers.trampoline();
		trampoline.createWorker().schedule(()->{
			System.out.println("123");
		});
		Schedulers.single();
 		Schedulers.newThread();
 		Schedulers.computation();
 		Schedulers.io();

		System.out.println("---------async-----------");

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

	@Data
	static class Content{
		private String data;
	}
}
