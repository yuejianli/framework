package com.yjl.amqp.service;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ConditionalOnEnabledResourceChain;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName:SendMessageServiceImpl
 * @Description TODO
 * @Author 岳建立
 * @Date 2020/12/22 14:10
 * @Version 1.0
 **/
@Service
public class SendMessageServiceImpl implements SendMessageService {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbit.direct.queue1}")
    private String queueName;


    @Value("${rabbit.fanout.exchange}")
    private String fanoutExchange;

    @Value("${rabbit.topic.exchange}")
    private String topicExchange;

    @Value("${rabbit.direct.exchange}")
    private String directExchange;


    // 最普通的.

    @Override
    public void sendQueue(Integer randNum) {
        rabbitTemplate.convertAndSend(queueName, String.valueOf(randNum));
    }


    @Override
    public void sendWork() {
        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend(queueName, "第" + i + "条消息,消息内容是:" + i);
        }
    }


    @Override
    public void fanout() {
        for (int i = 1; i <= 5; i++) {
            rabbitTemplate.convertAndSend(fanoutExchange, "", "fanout 发送消息:" + i);
        }
    }


    @Override
    public void direct() {
        rabbitTemplate.convertAndSend(directExchange, "debug", "debug 消息");
        rabbitTemplate.convertAndSend(directExchange, "info", "info 消息");
        rabbitTemplate.convertAndSend(directExchange, "warn", "warn 消息");
        rabbitTemplate.convertAndSend(directExchange, "error", "error 消息");
    }


    @Override
    public void topic() {

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

                rabbitTemplate.convertAndSend(topicExchange, routingKey,
                        message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
