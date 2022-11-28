package com.yjl.amqp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ClassName:App
 * @Description TODO
 * @Author 岳建立
 * @Date 2020/12/22 13:26
 * @Version 1.0
 **/
@SpringBootApplication
public class ServerApp {
    public static void main(String[] args) {
        SpringApplication.run(ServerApp.class, args);
        System.out.println(">>>>>>>>>>>RabbitMQ 的服务器端启动");
    }
}
