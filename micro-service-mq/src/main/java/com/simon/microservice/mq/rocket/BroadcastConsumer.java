package com.simon.microservice.mq.rocket;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.List;

/**
 * @Author simon
 * @Date 2020/4/27 2:21 下午
 */
public class BroadcastConsumer {

	public static void main(String[] args) throws Exception {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("ProducerGroupName");
		consumer.setNamesrvAddr("localhost:9876");
		consumer.setMessageModel(MessageModel.BROADCASTING);
		consumer.subscribe("TopicTest", "TagA || TagB || TagC");

		consumer.registerMessageListener(new MessageListenerConcurrently() {
			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list,
					ConsumeConcurrentlyContext consumeConcurrentlyContext) {
				System.out.println(Thread.currentThread().getName() + "Receive new message " + list);
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});

		consumer.start();
		System.out.println("Broadcast consumer start ");
	}
}
