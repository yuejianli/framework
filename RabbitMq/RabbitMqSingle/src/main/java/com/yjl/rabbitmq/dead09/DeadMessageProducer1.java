package com.yjl.rabbitmq.dead09;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.yjl.rabbitmq.util.ConnectionFactoryUtil;

import java.util.Scanner;

/**
 * @ClassName:work
 * @Description 消息发布者
 * @Author 岳建立
 * @Date 2020/12/22 19:55
 * @Version 1.0
 * 死信队列， 设置过期时间。
 * <p>
 * 先从概念解释上搞清楚这个定义，死信，顾名思义就是无法被消费的消息，字面意思可以这样理
 * 解，一般来说，producer 将消息投递到 broker 或者直接到 queue 里了，consumer 从 queue 取出消息
 * 进行消费，但某些时候由于特定的原因导致 queue 中的某些消息无法被消费，这样的消息如果没有
 * 后续的处理，就变成了死信，有死信自然就有了死信队列。
 * 应用场景:为了保证订单业务的消息数据不丢失，需要使用到 RabbitMQ 的死信队列机制，当消息
 * 消费发生异常时，将消息投入死信队列中.还有比如说: 用户在商城下单成功并点击去支付后在指定时
 * 间未支付时自动失效
 * <p>
 * 消息 TTL 过期
 * 队列达到最大长度(队列满了，无法再添加数据到 mq 中)
 * 消息被拒绝(basic.reject 或 basic.nack)并且 requeue=false.
 **/
public class DeadMessageProducer1 {
    private static String EXCHANGE_NAME = "normal_exchange";

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionFactoryUtil.createConnection();
        Channel channel = connection.createChannel();
        // 创建交换机， 交换机 名称是  logs
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT,
                true, false, null);

        // 设置消息的 TTL 过期时间  10s
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().expiration("10000").build();


        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            // routingKey 为 空
            String inputMessage = scanner.nextLine();

            // 设置过期时间
            channel.basicPublish(EXCHANGE_NAME, "zhangsan",
                    properties, inputMessage.getBytes("UTF-8"));
        }
        channel.close();
        connection.close();
    }
}
