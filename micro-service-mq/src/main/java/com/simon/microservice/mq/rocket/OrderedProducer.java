package com.simon.microservice.mq.rocket;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.List;

/**
 * 顺序消息
 *  RocketMq 提供使用先进先出算法的顺序消息实现
 * 
 * @author simon
 * @date 2020/4/27 11:40 上午
 */
public class OrderedProducer {
	public static void main(String[] args) throws Exception {
		DefaultMQProducer producer = new DefaultMQProducer("ProducerGroupName");
		producer.setNamesrvAddr("localhost:9876");
		producer.start();

		String[] tags = new String[] {"TagA", "TagB", "TagC", "TagD", "TagE"};

		for (int i = 0; i < 100; i++) {
			int orderId = i % 100;
			Message message = new Message("TopicTest", tags[i & (tags.length - 1)], "Key" + i,
					("Hello RocketMq _" + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
			SendResult send = producer.send(message, new MessageQueueSelector() {
				// 消息队列选择器
				@Override
				public MessageQueue select(List<MessageQueue> list, Message message, Object o) {
					// 指定 message queue
					Integer id = (Integer) o;
					return list.get(id & (list.size() - 1));
				}
			}, orderId);

			System.out.println(send);
		}


		producer.shutdown();
	}
}
