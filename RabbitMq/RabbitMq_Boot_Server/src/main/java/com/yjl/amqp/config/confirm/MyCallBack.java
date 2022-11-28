package com.yjl.amqp.config.confirm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 回调配置
 *
 * @author yuejianli
 * @date 2022-11-24
 */
@Component
@Slf4j
public class MyCallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {


    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = Optional.ofNullable(correlationData).map(n -> n.getId()).orElse("0");
        if (ack) {
            log.info("交换机已经收到 id 为:{}的消息", id);
        } else {
            log.info("交换机还未收到 id 为:{}消息,由于原因:", id, cause);
        }
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("消费 {} 被退回,退回原因是:{}, 交换机是:{},路由key是:{}", new String(message.getBody()), replyText, exchange, routingKey);
    }
}
