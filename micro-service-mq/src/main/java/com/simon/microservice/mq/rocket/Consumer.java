package com.simon.microservice.mq.rocket;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;

/**
 * @author simon
 * @date 2020/4/26 15:58
 * @describe 倾我一生一世念, 来似飞花散似烟
 */
public class Consumer {

	public static void main(String[] args) throws MQClientException {
		/**
		 * DefaultMQPushConsumer
		 */
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("ProducerGroupName");

		consumer.setNamesrvAddr("localhost:9871");
		consumer.subscribe("TopicTest", "*");
		consumer.registerMessageListener(new MessageListenerOrderly() {
			@Override
			public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list,
					ConsumeOrderlyContext consumeOrderlyContext) {

				List<String> collect = list.stream().map(messageExt -> {
					try {
						return new String(messageExt.getBody(), "UTF-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					return "";
				}).collect(Collectors.toList());
				System.out.println(collect);
				return ConsumeOrderlyStatus.SUCCESS;
			}
		});

		consumer.start();
		System.out.println("consumer start");
	}


	public void method (){

	}




	/**
	 * DefaultMQPullConsumer<br/>
	 * 逐个读取某topic下所有 MessageQueue的内容 读完一遍后退出 主要处理额外的三件事情<br/>
	 * 1.获取MessageQueue并遍历<br/>
	 * 	 -> 一个topic包括多个MessageQueue 如果这个Consumer需要获取Topic下所有的消息,就要遍历多个的MessageQueue<br/>
	 * 	 -> 如果有特殊情况,也可以选择某些特定的MessageQueue来读取信息<br/>
	 * 2.维护Offset store
	 * 	 -> 从一个message Queue里拉取消息的时候,要传入Offset参数,随着不断读取信息,Offset会不断增长,这个时候由用户负责把Offset<br/>
	 * 	 -> 存储下来,根据具体情况可以存到内存里,写到磁盘或者数据库里<br/>
	 * 3.根据不同的消息状态做不同的处理<br/>
	 * 	 -> 拉取信息的请求发出后,会返回:FOUND,NO_MATCHED_MSG,NO_NEW_MSG,OFFSET_ILLEGAL四种状态,需要根据每个状态做不同的处理,<br/>
	 * 	 -> 比较重要的两个状态是FOUND和NO_NEW_MSG,分别表示获取到消息和没有新的消息
	 * 
	 * 
	 */
	public void defaultMqPullConsumer() throws Exception {
		DefaultMQPullConsumer consumer = new DefaultMQPullConsumer("ProducerGroupName");
		consumer.start();
		Set<MessageQueue> messageQueues = consumer.fetchSubscribeMessageQueues("TopTest");
		for (MessageQueue messageQueue : messageQueues) {
			long offset = consumer.fetchConsumeOffset(messageQueue, true);
			System.out.printf("consumer from the queue :" + messageQueue + "%n");

			single_mq: while (true) {
				try {
					PullResult pullResult = consumer.pullBlockIfNotFound(messageQueue, null,
							getMessageQueueOffset(messageQueue), 32);
					System.out.printf("%s%n", pullResult);
					putMessageQueueOffset(messageQueue, pullResult.getNextBeginOffset());
					switch (pullResult.getPullStatus()) {
						case FOUND:
							break;
						case NO_NEW_MSG:
							break;
						case NO_MATCHED_MSG:
							break;
						case OFFSET_ILLEGAL:
							break;
						default:
							break;
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		consumer.shutdown();
	}

	private static final Map<MessageQueue, Long> OFFSET_TABLE = new HashMap<>();

	private long getMessageQueueOffset(MessageQueue messageQueue) {
		Long offset = OFFSET_TABLE.get(messageQueue);
		return offset == null ? 0 : offset;
	}

	private void putMessageQueueOffset(MessageQueue messageQueue, Long offset) {
		OFFSET_TABLE.put(messageQueue, offset);
	}
}
