package com.yjl.rabbitmq.dem08;

import com.rabbitmq.client.*;
import com.yjl.rabbitmq.util.ConnectionFactoryUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName:MessageProducerTest3
 * @Description FANOUT  形式的消息信息
 * <p>
 * 消息持久化和设置优先级
 * @Author 岳建立
 * @Date 2020/12/20 16:35
 * @Version 1.0
 **/
public class MessageProducerTest2 {
    public static void main(String[] args) {
        //3. 创建连接
        try {
            Connection connection = ConnectionFactoryUtil.createConnection();
            //4. 创建 Channel
            Channel channel = connection.createChannel();
            //5. 创建交换机
            String exchangeName = "RabbitMQ-2";
            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT, true, false, null);

            String queueName = "queue2";

            boolean durable = true;
            channel.queueDeclare(queueName, durable, false, false, null);
            // 将交换机与 队列进行绑定
            channel.queueBind(queueName, exchangeName, "queue2_RabbitMq-2");


            /**
             * 让发布的消息持久化
             */
            // 1  deliveryMode 1 为消息不持久化  2 为消息持久化
          /*  AMQP.BasicProperties basicProperties=new AMQP.BasicProperties.Builder().deliveryMode(2)
                    .build();
            channel.basicPublish(exchangeName,routingKey,basicProperties,"你好啊".getBytes("UTF-8"));*/

            /**
             * 如果单纯的 进行持久化和优先级，那么可以用 MessageProperties 枚举类
             *  deliveryMod 为2表示持久化
             *  priority 为0 表示最低优先级
             *  contentType 为 text/palin 表示文本消息
             *
             */
            //AMQP.BasicProperties basicProperties=MessageProperties.PERSISTENT_TEXT_PLAIN;

            /**
             * 优先级   priority 0~9 之间，数字越大，优先级越高。
             * 需要设置 优先级队列。  即在创建队列时，设置队列的参数
             *
             */
            AMQP.BasicProperties basicProperties = new AMQP.BasicProperties.Builder()
                    .priority(9)
                    .build();
            channel.basicPublish(exchangeName, "", basicProperties, "你好啊".getBytes("UTF-8"));

            channel.basicPublish(exchangeName, "", basicProperties, "Hello World".getBytes("UTF-8"));

            System.out.println("发布消息成功");
            channel.close();
            connection.close();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
