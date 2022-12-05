package com.yjl.rabbitmq.direct04;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.yjl.rabbitmq.util.ConnectionFactoryUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @ClassName:work
 * @Description 消息发布者
 * @Author 岳建立
 * @Date 2020/12/22 19:55
 * @Version 1.0
 * <p>
 * direct 直连， routingKey 路由 key 一致的，才进行发送。
 * <p>
 * console 接收  debug info warn error
 * file 接收  info warn error
 * db 接收  warn error
 * routingKey 有值。
 **/
public class DirectMessageProducer {
    private static String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionFactoryUtil.createConnection();
        Channel channel = connection.createChannel();
        // 创建交换机， 交换机 名称是  logs
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT,
                true, false, null);

        // 定义一个路由 routingKey 和 信息 的map, 由 map 进行处理。

        Map<String, String> messageMap = new HashMap<>();

        messageMap.put("debug", "一个 debug 消息 1");
        messageMap.put("debug", "一个 debug 消息 2");

        messageMap.put("info", "一个 info 消息 1");
        messageMap.put("info", "一个 info 消息 2");
        messageMap.put("info", "一个 info 消息3 ");

        messageMap.put("warn", "一个 warn 消息 1");
        messageMap.put("warn", "一个 warn 消息 2");
        messageMap.put("warn", "一个 warn 消息 3");

        messageMap.put("error", "一个 error 消息 1");
        messageMap.put("error", "一个 error 消息 2");
        messageMap.put("error", "一个 error 消息 3");
        messageMap.put("error", "一个 error 消息 4");

        messageMap.forEach((routingKey, message) -> {
            try {
                channel.basicPublish(EXCHANGE_NAME, routingKey,
                        null, message.getBytes("UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //输入流等待
        System.in.read();

        channel.close();
        connection.close();


//        Scanner scanner = new Scanner(System.in);
//          // 传入的格式为  debug 一个 debug 消息 1
//        while (scanner.hasNextLine()) {
//            // routingKey 为 空
//            String inputMessage = scanner.nextLine();
//
//            String[] splitMessage = inputMessage.split("\\ ");
//
//            channel.basicPublish(EXCHANGE_NAME, splitMessage[0],
//                    null, splitMessage[1].getBytes("UTF-8"));
//        }
//        channel.close();
//        connection.close();
    }
}
