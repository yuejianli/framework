package com.yjl.amqp.service;

/**
 * @ClassName:SendMessageService
 * @Description TODO
 * @Author 岳建立
 * @Date 2020/12/22 14:10
 * @Version 1.0
 **/
public interface SendMessageService {

    void sendQueue(Integer randNum);

    void sendWork();

    void fanout();

    void direct();

    void topic();
}
