package com.yjl.amqp.controller;

import com.yjl.amqp.service.ReceiveMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ClassName:ReceiveMessageController
 * @Description TODO
 * @Author 岳建立
 * @Date 2020/12/23 9:27
 * @Version 1.0
 **/
@RestController
@RequestMapping("/receive")
public class ReceiveMessageController {

    @Resource
    private ReceiveMessageService receiveMessageService;

    @GetMapping("/getQueue")
    public String getQueue() {
        String message = receiveMessageService.getQueueMessage();
        return "获取消息队列中的消息:" + message;
    }
}
