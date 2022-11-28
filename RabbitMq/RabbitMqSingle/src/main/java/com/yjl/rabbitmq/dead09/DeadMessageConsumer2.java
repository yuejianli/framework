package com.yjl.rabbitmq.dead09;

import com.rabbitmq.client.*;
import com.yjl.rabbitmq.util.ConnectionFactoryUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName:WorkMessageConsumer1
 * @Description 消息消费者1
 * 控制台 要  debug  info warn  error
 * @Author 岳建立
 * @Date 2020/12/22 20:02
 * @Version 1.0
 **/
public class DeadMessageConsumer2 {
    private static String EXCHANGE_NAME = "normal_exchange";
    private static String DEAD_EXCHANGE_NAME = "dead_exchange";


    private static String QUEUE_NAME = "normal_queue";
    private static String DEAD_QUEUE_NAME = "dead_queue";

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionFactoryUtil.createConnection();
        final Channel channel = connection.createChannel();

        //1. 创建死亡队列和绑定关系
        channel.exchangeDeclare(DEAD_EXCHANGE_NAME,
                BuiltinExchangeType.DIRECT, true, false, null);
        channel.queueDeclare(DEAD_QUEUE_NAME, true, false, false, null);
        channel.queueBind(DEAD_QUEUE_NAME, DEAD_EXCHANGE_NAME, "lisi");


        //2. 创建活着的队列和绑定
        channel.exchangeDeclare(EXCHANGE_NAME,
                BuiltinExchangeType.DIRECT, true, false, null);

        Map<String, Object> params = new HashMap();

        // 死信交换机 exchange
        params.put("x-dead-letter-exchange", DEAD_EXCHANGE_NAME);
        // 死信交换机的 routing-key
        params.put("x-dead-letter-routing-key", "lisi");

        // 设置正常队列的长度 是 添加的方法。
        // 需要先将队列删除，再重新构建。
        params.put("x-max-length", 6);

        // 创建队列时， 绑定 死信队列的信息。
        channel.queueDeclare(QUEUE_NAME, true, false, false, params);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "zhangsan");


        // 打印到控制台
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println(QUEUE_NAME + "获取级别:" + message.getEnvelope().getRoutingKey() + ">>>  消息 :" + new String(message.getBody()));
        };

        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println(QUEUE_NAME + ">>>>> 中断了消息接收 " + consumerTag);
        };

        TimeUnit.SECONDS.sleep(8);
        channel.basicConsume(
                QUEUE_NAME, true, deliverCallback, cancelCallback);

        //输入流等待
        System.in.read();
        //关闭
        channel.close();
        connection.close();
    }
}
