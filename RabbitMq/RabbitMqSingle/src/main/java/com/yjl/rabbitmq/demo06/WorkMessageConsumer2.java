package com.yjl.rabbitmq.demo06;

import com.rabbitmq.client.*;
import com.yjl.rabbitmq.util.ConnectionFactoryUtil;
import lombok.SneakyThrows;

import java.io.IOException;

/**
 * @ClassName:WorkMessageConsumer1
 * @Description 消息消费者2
 * @Author 岳建立
 * @Date 2020/12/22 20:02
 * @Version 1.0
 **/
public class WorkMessageConsumer2 {
    public static final String QUEUE_NAME = "work_queue";
    public static final String EXCHANGE_NAME = "work_exchange";

    public static void main(String[] args) throws Exception {
        //3.创建连接
        Connection connection = ConnectionFactoryUtil.createConnection();
        //4. 创建信道
        final Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT,
                true, false, null);

        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        // 进行绑定

        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "work_exchange_queue");


        //6.获取消息
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @SneakyThrows
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                Thread.sleep(2000);
                //结果也是各分5个。
                System.out.println(consumerTag + " 消费者获取相应的信息,获取的内容是:" + new String(body));
            }
        };
        channel.basicConsume("work_queue", true, "消费者2", defaultConsumer);
        //输入流等待
        System.in.read();
        //关闭
        channel.close();
        connection.close();
    }
}
