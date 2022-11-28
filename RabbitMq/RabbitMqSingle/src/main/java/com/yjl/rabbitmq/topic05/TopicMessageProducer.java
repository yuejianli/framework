package com.yjl.rabbitmq.topic05;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.yjl.rabbitmq.util.ConnectionFactoryUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName:work
 * @Description 消息发布者
 * @Author 岳建立
 * @Date 2020/12/22 19:55
 * @Version 1.0
 * <p>
 * topic 支持表达式的写法。
 * <p>
 * 发送到类型是 topic 交换机的消息的 routing_key 不能随意写,必须满足一定的要求，它必须是一个单
 * 词列表，以点号分隔开。这些单词可以是任意单词，比如说："stock.usd.nyse", "nyse.vmw",
 * "quick.orange.rabbit".这种类型的。当然这个单词列表最多不能超过 255 个字节。
 * 在这个规则列表中，其中有两个替换符是大家需要注意的
 * *(星号)可以代替一个单词
 * #(井号)可以替代零个或多个单词
 * q1: 中间带 orange 带 3 个单词的字符串(*.orange.*)
 * <p>
 * q2: Q2-->绑定的是
 * 最后一个单词是 rabbit 的 3 个单词(*.*.rabbit)
 * 第一个单词是 lazy 的多个单词(lazy.#)
 * <p>
 * 当队列绑定关系是下列这种情况时需要引起注意
 * 当一个队列绑定键是#,那么这个队列将接收所有数据，就有点像 fanout 了
 * 如果队列绑定键当中没有#和*出现，那么该队列绑定类型就是 direct 了
 **/
public class TopicMessageProducer {
    private static String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionFactoryUtil.createConnection();
        Channel channel = connection.createChannel();
        // 创建交换机， 交换机 名称是  logs
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC,
                true, false, null);

        // 定义一个路由 routingKey 和 信息 的map, 由 map 进行处理。

        Map<String, String> messageMap = new HashMap<>();

        messageMap.put("quick.orange.rabbit", "被队列 Q1Q2 接收到");
        messageMap.put("lazy.orange.elephant", "被队列 Q1Q2 接收到");

        messageMap.put("quick.orange.fox", "被队列 Q1 接收到");
        messageMap.put("lazy.brown.fox", "被队列 Q2 接收到");
        messageMap.put("info", "一个 info 消息3 ");

        messageMap.put("lazy.pink.rabbit", "虽然满足两个绑定但只被队列 Q2 接收一次");
        messageMap.put("quick.brown.fox", "不匹配任何绑定不会被任何队列接收到会被丢弃");
        messageMap.put("quick.orange.male.rabbit", "是四个单词不匹配任何绑定会被丢弃");

        messageMap.put("lazy.orange.male.rabbit", "是四个单词但匹配 Q2");

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
//        while(scanner.hasNextLine()) {
//            // routingKey 为 空
//            String inputMessage = scanner.nextLine();
//
//            String[] splitMessage = inputMessage.split("\\ ");
//
//            channel.basicPublish(EXCHANGE_NAME,splitMessage[0],
//                    null,splitMessage[1].getBytes("UTF-8"));
//        }
//        channel.close();
//        connection.close();
    }
}
