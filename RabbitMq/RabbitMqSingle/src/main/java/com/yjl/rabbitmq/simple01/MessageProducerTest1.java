package com.yjl.rabbitmq.simple01;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName:MessageProducer
 * @Description 简单的创建 连接信息 ConnectionFactory
 * @Author 岳建立, 虚拟主机是  /eagle2_host,  带有 /, 如果创建的虚拟主机是 yjl 的话，则为 yjl
 * @Date 2020/12/20 14:25
 * @Version 1.0
 **/
@Slf4j
public class MessageProducerTest1 {
    public static void main(String[] args) {
        //1. 创建工厂  是  rabbitmq 下面的 ConnectionFactory
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //2. 设置工厂的具体的信息
        //设置主机
        connectionFactory.setHost("127.0.0.1");
        //设置虚拟主机
        connectionFactory.setVirtualHost("/eagle2_host");
        //设置端口
        connectionFactory.setPort(5672);
        //设置用户名
        connectionFactory.setUsername("guest");
        //设置密码
        connectionFactory.setPassword("guest");

        //3. 创建 Connection  是  com.rabbitmq.client 下面的接口
        try {
            Connection connection = connectionFactory.newConnection();
            // 输出连接
            System.out.println("输出连接:" + connection.toString());
            //关闭连接
            if (connection != null) {
                // 关闭
                connection.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }
}
