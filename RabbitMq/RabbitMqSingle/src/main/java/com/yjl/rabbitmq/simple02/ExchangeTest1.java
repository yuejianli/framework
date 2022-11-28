package com.yjl.rabbitmq.simple02;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import com.yjl.rabbitmq.util.ConnectionFactoryUtil;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * Exchange 交换机
 *
 * @author yuejianli
 * @date 2022-11-22
 */
public class ExchangeTest1 {
    public static void main(String[] args) {
        // 发布消息

        Connection connection = ConnectionFactoryUtil.createConnection();
        Channel channel = null;
        try {
            channel = connection.createChannel();

            // 交换机 有四种类型
            // 直接(direct), 主题(topic) ,标题(headers) , 扇出(fanout)

            // 创建临时队列, 队列的名称。
            String queueName = channel.queueDeclare().getQueue();


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
