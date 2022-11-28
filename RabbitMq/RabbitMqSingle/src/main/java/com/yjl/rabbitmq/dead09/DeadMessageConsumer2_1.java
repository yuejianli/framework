package com.yjl.rabbitmq.dead09;

import com.rabbitmq.client.*;
import com.yjl.rabbitmq.util.ConnectionFactoryUtil;

/**
 * @ClassName:WorkMessageConsumer1
 * @Description 消息消费者1
 * 控制台 要  debug  info warn  error
 * @Author 岳建立
 * @Date 2020/12/22 20:02
 * @Version 1.0
 **/
public class DeadMessageConsumer2_1 {
    private static String EXCHANGE_NAME = "dead_exchange";
    private static String QUEUE_NAME = "dead_queue";

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionFactoryUtil.createConnection();
        final Channel channel = connection.createChannel();
        //1. 创建交换器
        channel.exchangeDeclare(EXCHANGE_NAME,
                BuiltinExchangeType.DIRECT, true, false, null);
        //2.  创建队列
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        //3. 绑定队列与 交换机。  其中 routingKey 为 debug,info,warn,error  ,可以绑定多个。

        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "lisi");


        // 打印到控制台
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println(QUEUE_NAME + "获取级别:" + message.getEnvelope().getRoutingKey() + ">>>  消息 :" + new String(message.getBody()));
        };

        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println(QUEUE_NAME + ">>>>> 中断了消息接收 " + consumerTag);
        };
        channel.basicConsume(
                QUEUE_NAME, true, deliverCallback, cancelCallback);

        //输入流等待
        System.in.read();
        //关闭
        channel.close();
        connection.close();
    }
}
