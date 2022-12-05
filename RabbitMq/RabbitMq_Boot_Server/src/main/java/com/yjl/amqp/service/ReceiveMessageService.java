package com.yjl.amqp.service;

/**
 * TODO 用途描述
 *
 * @author Yue Jianli
 * @date 2022-12-01
 */

public interface ReceiveMessageService {

    void handlerMessage(String message);
}
