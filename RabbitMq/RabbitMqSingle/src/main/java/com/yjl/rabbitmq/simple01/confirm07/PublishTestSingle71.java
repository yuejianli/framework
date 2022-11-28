package com.yjl.rabbitmq.simple01.confirm07;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.yjl.rabbitmq.util.ConnectionFactoryUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * 单个发布确认
 *
 * @author yuejianli
 * @date 2022-11-22
 */
@Slf4j
public class PublishTestSingle71 {
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
            channel.queueDeclare("YJLE", true, false, false, null);

            TimeInterval timer = DateUtil.timer();

            timer.start();
            // 1. 进行发布确认
            channel.confirmSelect();
            for (int i = 0; i < 1000; i++) {

                String message = "发布消息" + i;

                channel.basicPublish("", "YJLE", null, message.getBytes(StandardCharsets.UTF_8));

                // 是否进行了确认。 如果进行了确认。
                boolean flag = channel.waitForConfirms();

                if (flag) {
                    System.out.println(">>>> 发布消息" + message + "成功");
                }

            }

            //20 ms
            System.out.println(">>>> 共发布 1000 条消息，花费时间:" + timer.intervalMs());

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
