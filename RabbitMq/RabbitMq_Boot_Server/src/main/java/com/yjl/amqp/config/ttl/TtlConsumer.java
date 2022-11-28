package com.yjl.amqp.config.ttl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * ttl 死信队列的消费者
 *
 * @author yuejianli
 * @date 2022-11-23
 */
@Component
@Slf4j
public class TtlConsumer {

    /**
     * 死信队列
     */
    @RabbitListener(queues = {"${rabbit.ttl.y_dead_queue_d}"})
    public void deadQueueListener(String message) {
        log.info("死信队列获取消息:" + message);
        // 执行具体的业务操作
    }

    /**
     * 延迟队列
     */
    @RabbitListener(queues = {"${rabbit.ttl.delayed_queue}"})
    public void delayedQueueListener(String message) {
        log.info("延迟队列获取消息:" + message);
        // 执行具体的业务操作
    }
}
