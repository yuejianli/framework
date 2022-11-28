package com.yjl.amqp.config.confirm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 确认队列的消费者
 *
 * @author yuejianli
 * @date 2022-11-23
 */
@Component
@Slf4j
public class ConfirmConsumer {

    /**
     * 确认队列
     */
    @RabbitListener(queues = {"${rabbit.confirm.queue}"})
    public void receiveMsg(Message message) {
        String msg = new String(message.getBody());
        log.info("接受到队列 confirm.queue 消息:{}", msg);
    }


    /**
     * 备份队列
     * 备份交换机优先级高
     */
    @RabbitListener(queues = {"${rabbit.confirm.warn_queue}"})
    public void warnMsg(Message message) {
        String msg = new String(message.getBody());
        log.info("报警接收到不可接收的消息:{}", msg);
    }
}
