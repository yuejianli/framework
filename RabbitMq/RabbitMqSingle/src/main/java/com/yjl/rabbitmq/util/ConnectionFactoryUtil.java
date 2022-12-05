package com.yjl.rabbitmq.util;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * 创建 Connection 连接信息
 *
 * @author yuejianli
 * @date 2022-11-21
 */
@Slf4j
public class ConnectionFactoryUtil {

    public static final String MQTT_HOST = "127.0.0.1";
    public static final String MQTT_VIRTUAL_HOST = "yjl";
    public static final int MQTT_PORT = 5672;
    public static final String MQTT_USERNAME = "guest";
    public static final String MQTT_PASSWORD = "guest";

    public static Connection createConnection() {
        //1. 创建工厂  是  rabbitmq 下面的 ConnectionFactory
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //2. 设置工厂的具体的信息
        //设置主机
        connectionFactory.setHost(MQTT_HOST);
        //设置虚拟主机
        connectionFactory.setVirtualHost(MQTT_VIRTUAL_HOST);
        //设置端口
        connectionFactory.setPort(MQTT_PORT);
        //设置用户名
        connectionFactory.setUsername(MQTT_USERNAME);
        //设置密码
        connectionFactory.setPassword(MQTT_PASSWORD);

        //3. 创建 Connection  是  com.rabbitmq.client 下面的接口
        try {
            return connectionFactory.newConnection();
        } catch (Exception e) {
            log.error(">>>> 创建连接 失败 ", e);
            return null;
        }
    }
}
