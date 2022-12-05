package com.yjl.amqp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-12-01
 */
@Service
@Slf4j
public class ReceiveMessageServiceImpl implements ReceiveMessageService {


    @Override
    public void handlerMessage(String message) {
        log.info(">>>> 获取到消息 {},开始进行业务处理", message);
        // 接下来,就是具体的业务去处理这些消息了.
    }
}
