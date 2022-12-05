package com.yjl.amqp.config.direct;

import com.yjl.amqp.service.ReceiveMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-11-22
 */
@Component
@Slf4j
public class DirectMqConsumer {

    @Resource
    private ReceiveMessageService receiveMessageService;


    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue("${rabbit.direct.queue1}"),
                    exchange = @Exchange(type = "direct", name = "${rabbit.direct.exchange}"),
                    key = {"debug", "info", "warn", "error"}
            )
    })
    public void fanoutQueueConsumerConsole(String message) {
        log.info("控制台打印输出:" + message);
        receiveMessageService.handlerMessage("控制台打印输出 direct:" + message);
    }


    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue("${rabbit.direct.queue2}"),
                    exchange = @Exchange(type = "direct", name = "${rabbit.direct.exchange}"),
                    key = {"info", "warn", "error"}
            )
    })
    public void fanoutQueueConsumerFile(String message) {
        log.info("文件 打印输出:" + message);
        receiveMessageService.handlerMessage("文件打印输出 direct:" + message);
    }

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue("${rabbit.direct.queue3}"),
                    exchange = @Exchange(type = "direct", name = "${rabbit.direct.exchange}"),
                    key = {"warn", "error"}
            )
    })
    public void fanoutQueueConsumerDb(String message) {
        log.info("Db 打印输出:" + message);
        receiveMessageService.handlerMessage("DB 打印输出 direct:" + message);
    }

}
