package com.simon.microservice.mq.rocket;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * 广播模式
 *
 * 	广播就是向一个主题的所有订阅者发送同一条消息，如果你想让一个主题的所有订阅者收到消息，广播是一个很好的选择
 * @Author simon
 * @Date 2020/4/27 2:18 下午
 */
public class BroadcastProducer {

	public static void main(String[] args) throws Exception {
		DefaultMQProducer producer = new DefaultMQProducer("ProducerGroupName");
		producer.setNamesrvAddr("localhost:9876");
		producer.start();

		for (int i = 0; i < 100; i++) {
			Message message = new Message("TopicTest", "TagA", "OrderId188",
					"hello world".getBytes(RemotingHelper.DEFAULT_CHARSET));
			System.out.println(producer.send(message));

		}

	}
}
