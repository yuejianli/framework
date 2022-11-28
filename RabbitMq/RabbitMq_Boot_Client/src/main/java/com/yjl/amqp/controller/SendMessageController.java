package com.yjl.amqp.controller;

import com.yjl.amqp.service.SendMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ConditionalOnEnabledResourceChain;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ClassName:SendMessageController
 * @Description TODO
 * @Author 岳建立
 * @Date 2020/12/22 14:03
 * @Version 1.0
 **/
@RestController
@RequestMapping("/send")
public class SendMessageController {

    @Resource
    private SendMessageService sendMessageService;

    @RequestMapping("/queue")
    public String queue() {
        Integer randNum = (int) (Math.random() * 1000 + 1);
        sendMessageService.sendQueue(randNum);
        return "存储到队列中的数据是:" + randNum;

    }

    @RequestMapping("/work")
    public String work() {
        sendMessageService.sendWork();
        return "批量生成循环数字";
    }

    @RequestMapping("/fanout")
    public String fanout() {
        sendMessageService.fanout();
        return "fanout生成消息";
    }

    @RequestMapping("/direct")
    public String direct() {
        sendMessageService.direct();
        return "direct 生成消息";
    }

    @RequestMapping("/topic")
    public String topic() {
        sendMessageService.topic();
        return "topic 生成消息";
    }
}
