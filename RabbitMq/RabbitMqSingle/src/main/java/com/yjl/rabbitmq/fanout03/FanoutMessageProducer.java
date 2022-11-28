package com.yjl.rabbitmq.fanout03;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.yjl.rabbitmq.util.ConnectionFactoryUtil;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @ClassName:work
 * @Description fanout 广播消息发布者，routingKey 并没有值。
 * 三个消费者都可以接收到。
 * <p>
 * routingKey 为空。
 * @Author 岳建立
 * @Date 2020/12/22 19:55
 * @Version 1.0
 **/
public class FanoutMessageProducer {

    private static String EXCHANGE_NAME = "fanout_logs";

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionFactoryUtil.createConnection();
        Channel channel = connection.createChannel();
        // 创建交换机， 交换机 名称是  logs
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT,
                true, false, null);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            // routingKey 为 空
            channel.basicPublish(EXCHANGE_NAME, "",
                    null, new String("发布广播消息,内容为:" + scanner.next()).getBytes("UTF-8"));
        }
        channel.close();
        connection.close();
    }
}
