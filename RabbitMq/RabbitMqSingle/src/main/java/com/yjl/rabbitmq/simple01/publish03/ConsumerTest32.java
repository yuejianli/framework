package com.yjl.rabbitmq.simple01.publish03;

import com.rabbitmq.client.*;
import com.yjl.rabbitmq.util.ConnectionFactoryUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * 消息的消费者
 *
 * @author yuejianli
 * @date 2022-11-22
 */
@Slf4j
public class ConsumerTest32 {
    public static void main(String[] args) {

        Connection connection = ConnectionFactoryUtil.createConnection();
        Channel channel = null;
        try {
            channel = connection.createChannel();


            DeliverCallback deliverCallback = (consumerTag, message) -> {
                System.out.println(">>> 获取到消息 :" + new String(message.getBody()));

                // 执行具体的业务操作.

                System.out.println(">>>> 执行具体的业务操作");
            };

            CancelCallback cancelCallback = (consumerTag) -> {
                System.out.println(">>>>> 中断了消息接收 " + consumerTag);
            };
            channel.basicConsume(
                    "YJLA", true, deliverCallback, cancelCallback);
            System.out.println(">>> 接收消息成功");
//            channel.basicConsume("YJLA",true,new DefaultConsumer(channel){
//                @Override
//                public void handleDelivery(String conssumerTag, Envelope envelope,
//                                           AMQP.BasicProperties properties,
//                                           byte[] body) throws IOException {
//                    String content=new String(body,"UTF-8");
//                    System.out.println("消息正文:"+content);
//                }
//            });

            TimeUnit.SECONDS.sleep(10);
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
