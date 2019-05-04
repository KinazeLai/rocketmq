package org.apache.rocketmq.example;

import org.apache.rocketmq.client.producer.DefaultMQProducer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class MqConfig {

    public static String MQ_SRV_ADDR = "120.78.88.88:9876";

    public static DateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void setAddress(DefaultMQProducer producer){
        producer.setNamesrvAddr(MQ_SRV_ADDR);
    }
}
