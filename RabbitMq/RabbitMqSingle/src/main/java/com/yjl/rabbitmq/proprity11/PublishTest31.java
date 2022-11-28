package com.yjl.rabbitmq.proprity11;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.yjl.rabbitmq.util.ConnectionFactoryUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 消息生产者
 *
 * @author yuejianli
 * @date 2022-11-22
 */
@Slf4j
public class PublishTest31 {
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
            Map<String, Object> params = new HashMap<>();

            // 设置优先级 为 10
            params.put("x-max-priority", 10);

            // 设置模式，懒模式
            params.put("x-queue-mode", "lazy");


            channel.queueDeclare("YJLAA", true, false, false, params);

            // 设置优先级为 5
            AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().priority(5).build();
            for (int i = 0; i < 100; i++) {
                String message = "你好啊,岳泽霖" + i;
                if (i % 3 == 0) {
                    // 进行发布消息, 带有优先级。
                    channel.basicPublish("", "YJLAA", properties, message.getBytes(StandardCharsets.UTF_8));
                } else {
                    // 进行发布消息
                    channel.basicPublish("", "YJLAA", null, message.getBytes(StandardCharsets.UTF_8));
                }
            }

            System.in.read();

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
