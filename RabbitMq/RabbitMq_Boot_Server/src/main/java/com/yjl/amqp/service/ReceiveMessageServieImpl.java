package com.yjl.amqp.service;

import org.springframework.stereotype.Service;

/**
 * @ClassName:ReceiveMessageServieImpl
 * @Description TODO
 * @Author 岳建立
 * @Date 2020/12/23 9:28
 * @Version 1.0
 **/
@Service
public class ReceiveMessageServieImpl implements ReceiveMessageService {
    private String message;

    @Override
    public String getQueueMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
        System.out.println(">>>>>>service层获取消息:" + message);
    }
}
