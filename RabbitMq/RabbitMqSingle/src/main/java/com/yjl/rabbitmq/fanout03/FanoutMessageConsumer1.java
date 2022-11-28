package com.yjl.rabbitmq.fanout03;

import com.rabbitmq.client.*;
import com.yjl.rabbitmq.util.ConnectionFactoryUtil;

import java.io.IOException;

/**
 * @ClassName:WorkMessageConsumer1
 * @Description 广播消息消费者1
 * @Author 岳建立
 * @Date 2020/12/22 20:02
 * @Version 1.0
 **/
public class FanoutMessageConsumer1 {
    private static String EXCHANGE_NAME = "fanout_logs";
    private static String QUEUE_NAME = "debug_console";

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionFactoryUtil.createConnection();
        final Channel channel = connection.createChannel();
        //1. 创建交换器
        channel.exchangeDeclare(EXCHANGE_NAME,
                BuiltinExchangeType.FANOUT, true, false, null);
        //2.  创建队列
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        //7. 绑定队列与 交换机。  其中 routingKey 为 ""
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");

        // 打印到控制台
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println(QUEUE_NAME + ">>> 获取到消息 :" + new String(message.getBody()));
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
