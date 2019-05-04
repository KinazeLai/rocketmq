/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.rocketmq.example.zebra.ordermessage;

import java.io.UnsupportedEncodingException;
import java.util.List;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.example.MqConfig;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

public class Producer {
    public static void main(String[] args) throws UnsupportedEncodingException {
        try {
            MQProducer producer = new DefaultMQProducer("OrderProducer");
            ((DefaultMQProducer) producer).setNamesrvAddr(MqConfig.MQ_SRV_ADDR);
            ((DefaultMQProducer) producer).setRetryTimesWhenSendFailed(20);
//            ((DefaultMQProducer) producer).setDefaultTopicQueueNums(1000);
            producer.start();

            String[] tags = new String[] {"TagA", "TagB", "TagC", "TagD", "TagE"};
            for (int i = 0; i < 1000000; i++) {
                Message msg =
                        new Message("TopicTestOrder", tags[i % tags.length], "KEY" + i,
                                ("A " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
                SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
                    @Override
                    public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                        Integer id = (Integer) arg;
//                        int index = id % mqs.size();
                        return mqs.get(id);
                    }
                },2);

                System.out.printf("%s%n", sendResult);
            }

            for (int i = 0; i < 10; i++) {
                Message msg =
                        new Message("TopicTestOrder", tags[i % tags.length], "KEY" + i,
                                ("B " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
                SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
                    @Override
                    public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                        Integer id = (Integer) arg;
//                        int index = id % mqs.size();
                        return mqs.get(id);
                    }
                },1);

                System.out.printf("%s%n", sendResult);
            }

            for (int i = 0; i < 10; i++) {
                Message msg =
                        new Message("TopicTestOrder", tags[i % tags.length], "KEY" + i,
                                ("C " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
                SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
                    @Override
                    public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                        Integer id = (Integer) arg;
//                        int index = id % mqs.size();
                        return mqs.get(id);
                    }
                },2);

                System.out.printf("%s%n", sendResult);
            }
            producer.shutdown();
        } catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
