package com.simon.microservice.mq.rocket;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.io.UnsupportedEncodingException;

/**
 * 异步发送消息
 *
 * @author simon
 * @date 2020/4/26 16:50
 * @describe 倾我一生一世念, 来似飞花散似烟
 */
public class AsyncProducer {

	public static void main(String[] args) throws Exception {
		DefaultMQProducer producer = new DefaultMQProducer("ProducerGroupName");

		producer.setNamesrvAddr("localhost:9876");
		producer.start();
		producer.setRetryTimesWhenSendAsyncFailed(0);

		for (int i = 0; i < 100; i++) {
			producer.send(
					new Message("TopicTest", "TagA",
							("hello rocket" + i).getBytes(RemotingHelper.DEFAULT_CHARSET)),
					new SendCallback() {
						@Override
						public void onSuccess(SendResult sendResult) {
							System.out.println("success " + sendResult.getMsgId());
						}

						@Override
						public void onException(Throwable throwable) {
							throwable.printStackTrace();
						}
					});
		}
//		producer.shutdown();
	}
}
