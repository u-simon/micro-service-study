package com.simon.microservice.mq.rocket;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * 延时消息
 * 	 延时消息提供了一种不同于普通消息的实现形式---他们只会在设定的时限到了之后才会被递送出去
 *
 * @author simon
 * @date 2020/4/27 3:00 下午
 */
public class ScheduledMessageProducer {
	public static void main(String[] args) throws Exception {
		DefaultMQProducer producer = new DefaultMQProducer("ProducerGroupName");
		producer.setNamesrvAddr("localhost:9876");
		producer.start();

		for (int i = 0; i < 2; i++) {
			Message message = new Message("TopicTest",
					("Hello scheduled message" + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
			message.setDelayTimeLevel(4);
			System.out.println(System.currentTimeMillis() + " - " + producer.send(message));
		}

		producer.shutdown();
	}
}
