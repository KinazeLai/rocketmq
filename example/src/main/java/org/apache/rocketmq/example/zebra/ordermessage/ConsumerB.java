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

import ch.qos.logback.core.util.TimeUtil;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.example.MqConfig;

import java.util.List;

public class ConsumerB {

    public static void main(String[] args) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("OrderCustomerB");
        consumer.setNamesrvAddr(MqConfig.MQ_SRV_ADDR);

//        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        consumer.subscribe("TopicTestOrder", "*");

        consumer.registerMessageListener(new MessageListenerOrderly() {
//            AtomicLong consumeTimes = new AtomicLong(0);

            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
//                context.setAutoCommit(false);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for(MessageExt msg:msgs) {
                        System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(),
                                new String(msg.getBody()));
    //                this.consumeTimes.incrementAndGet();
    //                if ((this.consumeTimes.get() % 2) == 0) {
    //                    return ConsumeOrderlyStatus.SUCCESS;
    //                } else if ((this.consumeTimes.get() % 3) == 0) {
    //                    return ConsumeOrderlyStatus.ROLLBACK;
    //                } else if ((this.consumeTimes.get() % 4) == 0) {
    //                    return ConsumeOrderlyStatus.COMMIT;
    //                } else if ((this.consumeTimes.get() % 5) == 0) {
    //                    context.setSuspendCurrentQueueTimeMillis(3000);
    //                    return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
    //                }
                }

                return ConsumeOrderlyStatus.SUCCESS;
            }
        });

        consumer.start();
        System.out.printf("Consumer Started.%n");
    }

}
