package com.yjl.amqp.config.fanout;

import com.yjl.amqp.service.ReceiveMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * fanout 的消费
 *
 * @author yuejianli
 * @date 2022-11-22
 */
@Component
@Slf4j
public class FanoutMqConsumer {

    @Resource
    private ReceiveMessageService receiveMessageService;


    @RabbitListener(queues = {"${rabbit.fanout.queue1}", "${rabbit.fanout.queue2}"})
    public void fanoutQueueConsumer1An2(String message) {
        log.info("队列 fanout:" + message);
        receiveMessageService.setMessage("第一个消费者和第二个消费者获取消息 fanout:" + message);
    }

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue("${rabbit.fanout.queue3}"),
                    exchange = @Exchange(type = "fanout", name = "${rabbit.fanout.exchange}"),
                    key = {}
            )
    })
    public void fanoutQueueConsumer3(String message) {
        log.info("第三个消费者获取消息 fanout:" + message);
        receiveMessageService.setMessage("第三个消费者获取消息 fanout:" + message);
    }


}
