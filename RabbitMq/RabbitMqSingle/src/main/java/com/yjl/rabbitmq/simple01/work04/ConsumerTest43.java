package com.yjl.rabbitmq.simple01.work04;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import com.yjl.rabbitmq.util.ConnectionFactoryUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 消息的消费者
 *
 * @author yuejianli
 * @date 2022-11-22
 */
@Slf4j
public class ConsumerTest43 {
    public static void main(String[] args) {
        String consumer = "B:";
        Connection connection = ConnectionFactoryUtil.createConnection();
        Channel channel = null;
        try {
            channel = connection.createChannel();


            DeliverCallback deliverCallback = (consumerTag, message) -> {
                System.out.println(consumer + ">>> 获取到消息 :" + new String(message.getBody()));

                // 执行具体的业务操作.

                System.out.println(consumer + ">>>> 执行具体的业务操作");
            };

            CancelCallback cancelCallback = (consumerTag) -> {
                System.out.println(consumer + ">>>>> 中断了消息接收 " + consumerTag);
            };
            channel.basicConsume(
                    "YJLB", true, deliverCallback, cancelCallback);
            System.out.println(consumer + ">>> 接收消息成功");

            TimeUnit.SECONDS.sleep(20);
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
