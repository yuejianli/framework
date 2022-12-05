package com.yjl.rabbitmq.simple01;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import com.yjl.rabbitmq.util.ConnectionFactoryUtil;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * 发布消息， 持久化。
 * 包括 队列 持久化和消息持久化两种。
 *
 * @author yuejianli
 * @date 2022-11-22
 */

public class MessageProducerTest6 {
    public static void main(String[] args) {
        // 发布消息

        Connection connection = ConnectionFactoryUtil.createConnection();
        Channel channel = null;
        try {
            channel = connection.createChannel();

            // 先创建队列

            /**
             * queueDeclare 队列声明  共有五个参数
             * 1.队列名称
             * 2.队列里面的消息是否持久化 默认消息存储在内存中
             * 3.该队列是否只供一个消费者进行消费 是否进行共享 true 可以多个消费者消费
             * 4.是否自动删除 最后一个消费者端开连接以后 该队列是否自动删除 true 自动删除
             * 5.其他参数
             */
            boolean durable = true;
            channel.queueDeclare("YJLD", durable, false, false, null);

            // 设置为1， 保证 一个发布成功之后再发送下一个。 是不公平分发。
            channel.basicQos(1);

            // 设置为4
            channel.basicQos(4);


            // 进行发布消息
            // MessageProperties.PERSISTENT_TEXT_PLAIN  是消息 持久化
            channel.basicPublish("", "YJLA", MessageProperties.PERSISTENT_TEXT_PLAIN, "你好啊,岳泽霖".getBytes(StandardCharsets.UTF_8));

            System.out.println(">>> 发布消息成功");

            TimeUnit.SECONDS.sleep(3);
        } catch (Exception e) {
            System.out.println(">>>> 消息出错 " + e.getMessage());
        } finally {
            if (channel != null) {
                try {
                    channel.close();
                } catch (Exception e) {

                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {

                }
            }
        }
    }
}
