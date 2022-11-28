package com.yjl.rabbitmq.dem07;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.yjl.rabbitmq.util.ConnectionFactoryUtil;

/**
 * @ClassName:MessageProducerTest
 * @Description 消息生产者
 * @Author 岳建立
 * @Date 2020/12/21 15:18
 * @Version 1.0
 * <p>
 * basicGet  主动摘取消息。
 **/
public class MessageProducer {

    public static void main(String[] args) throws Exception {
        //3. 创建连接
        Connection connection = ConnectionFactoryUtil.createConnection();
        //4. 根据连接，创建信道 Channel
        Channel channel = connection.createChannel();

        //9. 发布消息
        String message = "你好啊,两个蝴蝶飞:" + System.currentTimeMillis();
        channel.basicPublish("", "amqp_queue", null,
                message.getBytes("UTF-8"));
        System.out.println("发送消息成功:" + message);
        //10 关闭
        channel.close();
        connection.close();
    }
}
