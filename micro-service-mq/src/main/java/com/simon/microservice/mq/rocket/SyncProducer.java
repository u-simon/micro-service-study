package com.simon.microservice.mq.rocket;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * 同步发送
 * 		可靠同步发送在众多场景中被使用,例如重要的通知消息,短信通知,短信营销系统
 * 
 * @author simon
 * @date 2020/4/26 14:21
 * @describe 倾我一生一世念, 来似飞花散似烟
 */
public class SyncProducer {

	public static void main(String[] args) throws Exception {
		// 创建producer 并且指定GroupName
		DefaultMQProducer producer = new DefaultMQProducer("ProducerGroupName");
		// 绑定 namesrvAddr
		producer.setNamesrvAddr("localhost:9876");
		producer.start();
		for (int i = 0; i < 100; i++) {
			// 发送消息
			SendResult send = producer.send(new Message("TopicTest", "TagA",
					("hello rocket" + i).getBytes(RemotingHelper.DEFAULT_CHARSET)));
			System.out.println(send);
		}

		producer.shutdown();
	}
}
