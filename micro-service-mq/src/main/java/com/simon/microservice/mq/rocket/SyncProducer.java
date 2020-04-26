package com.simon.microservice.mq.rocket;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * @author simon
 * @date 2020/4/26 14:21
 * @describe 倾我一生一世念, 来似飞花散似烟
 */
public class SyncProducer {

	public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("ProducerGroupName");

        producer.setNamesrvAddr("localhost:9876");
        producer.setCreateTopicKey("topicTest");
        producer.start();

        for (int i = 0; i < 100; i++) {
            SendResult send = producer.send(new Message("TopicTest", "TagA", ("hello rocket" + i).getBytes(RemotingHelper.DEFAULT_CHARSET)));
            System.out.println(send);
        }

        producer.shutdown();
    }
}
