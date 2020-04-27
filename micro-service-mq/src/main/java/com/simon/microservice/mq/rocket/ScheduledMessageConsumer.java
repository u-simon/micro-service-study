package com.simon.microservice.mq.rocket;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @author simon
 * @date 2020/4/27 3:03 下午
 */
public class ScheduledMessageConsumer {

	public static void main(String[] args) throws Exception {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("ProducerGroupName");
		consumer.setNamesrvAddr("localhost:9876");
		consumer.subscribe("TopicTest", "*");
		consumer.registerMessageListener(new MessageListenerConcurrently() {
			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list,
					ConsumeConcurrentlyContext consumeConcurrentlyContext) {
				for (MessageExt messageExt : list) {
					//Print approximate delay time period
					System.out.println(messageExt.getMsgId() + "->"
							+ (System.currentTimeMillis() - messageExt.getStoreTimestamp()));
				}
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});
		consumer.start();

		System.out.println("Scheduled message consumer start ");

	}
}
