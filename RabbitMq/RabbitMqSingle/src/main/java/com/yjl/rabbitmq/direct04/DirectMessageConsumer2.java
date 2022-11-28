package com.yjl.rabbitmq.direct04;

import cn.hutool.core.io.FileUtil;
import com.rabbitmq.client.*;
import com.yjl.rabbitmq.util.ConnectionFactoryUtil;

import java.io.IOException;
import java.util.Collections;

/**
 * @ClassName:WorkMessageConsumer1
 * @Description 消息消费者2
 * 文件里面 要  info  warn  error
 * @Author 岳建立
 * @Date 2020/12/22 20:02
 * @Version 1.0
 **/
public class DirectMessageConsumer2 {
    private static String EXCHANGE_NAME = "direct_logs";
    private static String QUEUE_NAME = "log_file";

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionFactoryUtil.createConnection();
        final Channel channel = connection.createChannel();
        //1. 创建交换器
        channel.exchangeDeclare(EXCHANGE_NAME,
                BuiltinExchangeType.DIRECT, true, false, null);
        //2.  创建队列
        channel.queueDeclare(QUEUE_NAME, true, false, true, null);
        //7. 绑定队列与 交换机。  其中 routingKey 为 info ,warn, error
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "info");
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "warn");
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "error");

        // 打印到控制台
        DeliverCallback deliverCallback = (consumerTag, message) -> {

            String fileMessage = QUEUE_NAME + "获取级别:" + message.getEnvelope().getRoutingKey() + ">>>  消息 :" + new String(message.getBody());

            FileUtil.appendUtf8Lines(Collections.singletonList(fileMessage), "D:\\rabbitMq\\log.log");

            System.out.println(">>>> 追加信息到 文件 里面, 内容是:" + fileMessage);
        };

        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println(QUEUE_NAME + ">>>>> 中断了消息接收 " + consumerTag);
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
        //输入流等待
        System.in.read();
        //关闭
        channel.close();
        connection.close();
    }
}
