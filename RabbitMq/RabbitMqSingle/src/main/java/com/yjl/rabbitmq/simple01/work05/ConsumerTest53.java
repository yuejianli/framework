package com.yjl.rabbitmq.simple01.work05;

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
public class ConsumerTest53 {
    public static void main(String[] args) {
        String consumer = "B:";
        Connection connection = ConnectionFactoryUtil.createConnection();
        Channel channel = null;
        try {
            channel = connection.createChannel();


            Channel finalChannel = channel;
            DeliverCallback deliverCallback = (consumerTag, message) -> {

                System.out.println(consumer + "收到消息，还没有确认" + new String(message.getBody()));
                // 应答时间长
                try {
                    TimeUnit.SECONDS.sleep(30);
                } catch (Exception e) {

                }
                // 消息应答
                finalChannel.basicAck(message.getEnvelope().getDeliveryTag(), true);

                System.out.println(consumer + ">>> 确认获取到消息 :" + new String(message.getBody()));
            };

            CancelCallback cancelCallback = (consumerTag) -> {
                System.out.println(consumer + ">>>>> 中断了消息接收 " + consumerTag);
            };
            channel.basicConsume(
                    "YJLC", false, deliverCallback, cancelCallback);
            System.out.println(consumer + ">>> 接收消息成功");

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
