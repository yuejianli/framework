package com.yjl.rabbitmq.simple01;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.yjl.rabbitmq.util.ConnectionFactoryUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName:MessageProducerTest2
 * @Description 创建连接信息, 创建 Channel
 * @Author 岳建立
 * @Date 2020/12/20 14:34
 * @Version 1.0
 **/
public class MessageProducerTest2 {
    public static void main(String[] args) {
        //3.创建 新的连接
        try {
            Connection connection = ConnectionFactoryUtil.createConnection();
            //4. 根据连接 Connection 创建信道 Channel
            Channel channel = connection.createChannel();
            //5. 关闭
            System.out.println("connection:" + connection);
            System.out.println("channel:" + channel);

            channel.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
