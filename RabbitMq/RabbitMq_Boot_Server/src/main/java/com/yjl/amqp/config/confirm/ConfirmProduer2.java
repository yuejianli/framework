package com.yjl.amqp.config.confirm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Optional;

/**
 * 生产者
 *
 * @author yuejianli
 * @date 2022-11-23
 */
@RestController
@Slf4j
public class ConfirmProduer2 implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    @Value("${rabbit.confirm.exchange}")
    private String exchange;

    @Value("${rabbit.confirm.routing-key}")
    private String routingKey;

    @Resource
    private RabbitTemplate rabbitTemplate;


    @PostConstruct
    public void initCallBack() {
        rabbitTemplate.setConfirmCallback(this);
        // 设置开启
        /**
         * true：
         * 交换机无法将消息进行路由时，会将该消息返回给生产者
         * false：
         * 如果发现消息无法进行路由，则直接丢弃
         */
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnCallback(this);
    }

    @GetMapping("/confirmSendMessage2/{message}")
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


    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = Optional.ofNullable(correlationData).map(n -> n.getId()).orElse("0");
        if (ack) {
            log.info(" 生产者处理, 交换机已经收到 id 为:{}的消息", id);
        } else {
            log.info("生产者处理, 交换机还未收到 id 为:{}消息,由于原因:", id, cause);
        }
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("生产者处理, 消费 {} 被退回,退回原因是:{}, 交换机是:{},路由key是:{}", new String(message.getBody()), replyText, exchange, routingKey);
    }
}
