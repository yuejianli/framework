package com.yjl.amqp.config.confirm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 生产者
 *
 * @author yuejianli
 * @date 2022-11-23
 */
@RestController
@Slf4j
public class ConfirmProduer {

    @Value("${rabbit.confirm.exchange}")
    private String exchange;

    @Value("${rabbit.confirm.routing-key}")
    private String routingKey;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private MyCallBack myCallBack;

//    @PostConstruct
//    public void initCallBack() {
//       rabbitTemplate.setConfirmCallback(myCallBack);
//    }

    @GetMapping("/confirmSendMessage/{message}")
    public String sendMessage(@PathVariable("message") String message) {
        log.info("接收到消息:{}", message);

        CorrelationData correlationData1 = new CorrelationData();
        correlationData1.setId("1");
        // 发送到 A 队列
        rabbitTemplate.convertAndSend(exchange, routingKey, "编号为1:" + message, correlationData1);

        CorrelationData correlationData2 = new CorrelationData("2");
        correlationData2.setId("2");
        //发送到B队列
        rabbitTemplate.convertAndSend(exchange, "12222", "编号为2:" + message, correlationData2);
        return message;
    }
}
