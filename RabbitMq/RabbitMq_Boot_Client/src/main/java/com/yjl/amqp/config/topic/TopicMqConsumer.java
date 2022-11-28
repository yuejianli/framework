package com.yjl.amqp.config.topic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * topic 的队列配置
 *
 * @author yuejianli
 * @date 2022-11-22
 */
// @Component
@Slf4j
public class TopicMqConsumer {


    @RabbitListener(queues = {"${rabbit.topic.queue1}"})
    public void fanoutQueueConsumer1An2(String message) {
        log.info("队列 topic:" + message);
    }


    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue("${rabbit.topic.queue2}"),
                    exchange = @Exchange(type = "topic", name = "${rabbit.topic.exchange}"),
                    key = {"lazy.#", "*.*.rabbit"}
            )
    })
    public void fanoutQueueConsumerConsole(String message) {
        log.info("file topic:" + message);
    }
}
