package com.yjl.amqp.service;

/**
 * @ClassName:ReceiveMessageService
 * @Description TODO
 * @Author 岳建立
 * @Date 2020/12/23 9:28
 * @Version 1.0
 **/
public interface ReceiveMessageService {

    String getQueueMessage();

    void setMessage(String message);

}
