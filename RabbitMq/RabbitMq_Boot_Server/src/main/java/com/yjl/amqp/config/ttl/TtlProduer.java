package com.yjl.amqp.config.ttl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 生产者
 *
 * @author yuejianli
 * @date 2022-11-23
 */
@RestController
@Slf4j
public class TtlProduer {

    @Value("${rabbit.ttl.x_exchange}")
    private String xexchange;


    @Value("${rabbit.ttl.delayed_exchange}")
    private String deadExchange;

    @Value("${rabbit.ttl.delayed_routing_key}")
    private String deadRoutingKey;


    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMessage/{message}")
    public String sendMessage(@PathVariable("message") String message) {
        log.info("接收到消息:{}", message);
        // 发送到 A 队列
        rabbitTemplate.convertAndSend(xexchange, "XA", "消息来自ttl 10s 的队列消息:" + message);
        //发送到B队列
        rabbitTemplate.convertAndSend(xexchange, "XB", "消息来自ttl 40s 的队列消息:" + message);
        return message;
    }


    @GetMapping("/sendMessage/{message}/{ttl}")
    public String sendMessageWithTtl(@PathVariable("message") String message, @PathVariable("ttl") String ttl) {
        log.info("接收到消息:{}, 发送的时长 是{} ms", message, ttl);
        //
//        // 发送到 A 队列
//        rabbitTemplate.convertAndSend(xexchange,"XA","消息来自ttl 10s 的队列消息:"+message);
//        //发送到B队列
//        rabbitTemplate.convertAndSend(xexchange,"XB","消息来自ttl 40s 的队列消息:"+message);


        // 发送给 C 队列
        rabbitTemplate.convertAndSend(xexchange, "XC", message, correlationData -> {
            correlationData.getMessageProperties().setExpiration(ttl);
            return correlationData;
        });
        return message;
    }


    @GetMapping("/sendDelayMessage/{message}/{ttl}")
    public String sendDelayMessageWithTtl(@PathVariable("message") String message, @PathVariable("ttl") String ttl) {
        log.info("接收到消息:{}, 发送的时长 是{} ms", message, ttl);

        // 发送给 C 队列
        rabbitTemplate.convertAndSend(deadExchange, deadRoutingKey, message, correlationData -> {
            correlationData.getMessageProperties().setExpiration(ttl);
            return correlationData;
        });
        return message;
    }


}
