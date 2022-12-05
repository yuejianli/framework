package com.yjl.rabbitmq.dem08;

import com.rabbitmq.client.*;
import com.yjl.rabbitmq.util.ConnectionFactoryUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName:MessageProducerTest3
 * @Description 发布消息，对 队列进行配置。
 * @Author 岳建立
 * <p>
 * 队列信息设置， 设置消息的过期时间等属性信息。
 * @Date 2020/12/20 16:35
 * @Version 1.0
 **/
public class MessageProducerTest3 {
    public static void main(String[] args) {
        //3. 创建连接
        try {
            Connection connection = ConnectionFactoryUtil.createConnection();
            //4. 创建 Channel
            Channel channel = connection.createChannel();
            //5. 创建交换机
            String exchangeName = "RabbitMQ-3";
            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT, true, false, null);

            String queueName = "queue3";
            /**
             * 设置优先级队列
             */
            Map<String, Object> argumentMap = new HashMap<>();
            argumentMap.put("x-max-priority", 9);
            // 2. 设置队列的过期时间
            argumentMap.put("x-expires", 20000);
            //3. 设置队列消息的过期时间
            argumentMap.put("x-message-ttl", 15000);
            //4. 消息可以无限积压? 可以传输任意大小?
            // 队列最大积压消息数量限制
            argumentMap.put("x-max-length", 10);
            // 单消息最大字节数量
            argumentMap.put("x-max-length-bytes", 1024);
            // 超过时，会删除头部   drop-head  默认
            // 拒绝消息加入时   x-overflow   value值为 reject-publish 拒绝加入
            argumentMap.put("x-overflow", "reject-publish");
            channel.queueDeclare(queueName, true, false, false, argumentMap);


            //绑定 消息
            channel.queueBind(queueName, exchangeName, "queue2_RabbitMq-3");

            /**
             * 设置 TTL 自动删除过期
             * 在 RabbitMQ 中，有三个层面。
             * 1. 设置某个单独消息的过期时间
             * 2. 设置某个队列的过期时间
             * 3. 设置某个队列中消息的过期时间(相当于统一设置队列中所有消息的过期时间)
             *
             */
            //1. 单消息的过期时间  20s
            AMQP.BasicProperties basicProperties = new AMQP.BasicProperties.Builder()
                    .expiration("20000")
                    .build();
            channel.basicPublish(exchangeName, "", basicProperties, "你好啊".getBytes("UTF-8"));

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
