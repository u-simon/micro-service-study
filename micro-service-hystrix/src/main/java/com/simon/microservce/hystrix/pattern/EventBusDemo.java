package com.simon.microservce.hystrix.pattern;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

/**
 * @author simon
 * @date 2019/12/20 19:54
 * @describe 一念花开, 一念花落
 */
public class EventBusDemo {

	/**
	 * 事件类(被观察者)
	 */
	static class Event {

	}

	/**
	 * 事件监听器(观察者)
	 */
	static class EventListener {

		// @Subscribe 标记这个方法为事件处理方法,并且该方法只能有一个参数(事件类)
		@Subscribe
		public void handle(Event event) {
			System.out.println("handle finish");
		}
	}

	public static void main(String[] args) {
		EventBus demo = new EventBus("demo");
		// 注册监听器
		demo.register(new EventListener());
		demo.register(new EventListener());

		// 发布事件
		demo.post(new Event());
		demo.post(new Event());

	}
}
