package com.yjl.rabbitmq.dead09;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.yjl.rabbitmq.util.ConnectionFactoryUtil;

import java.util.Scanner;

/**
 * @ClassName:work
 * @Description 消息发布者
 * @Author 岳建立
 * @Date 2020/12/22 19:55
 * @Version 1.0
 * 死信队列， 设置长度。
 **/
public class DeadMessageProducer2 {
    private static String EXCHANGE_NAME = "normal_exchange";

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionFactoryUtil.createConnection();
        Channel channel = connection.createChannel();
        // 创建交换机， 交换机 名称是  logs
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT,
                true, false, null);

        for (int i = 0; i < 10; i++) {
            // 设置过期时间
            String message = "发送消息" + i;
            channel.basicPublish(EXCHANGE_NAME, "zhangsan",
                    null, message.getBytes("UTF-8"));
        }
        System.in.read();
        channel.close();
        connection.close();
    }
}
