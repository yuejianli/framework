package com.yjl.rabbitmq.simple01.work04;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.yjl.rabbitmq.util.ConnectionFactoryUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * 消息生产者
 * <p>
 * 一个队列，关联两个消费者。 会一次使用一个。
 *
 * @author yuejianli
 * @date 2022-11-22
 */
@Slf4j
public class PublishTest41 {
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
            channel.queueDeclare("YJLB", true, false, false, null);

            Scanner scanner = new Scanner(System.in);

            while (scanner.hasNext()) {
                // 进行发布消息
                channel.basicPublish("", "YJLB", null, scanner.next().getBytes(StandardCharsets.UTF_8));
            }
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
