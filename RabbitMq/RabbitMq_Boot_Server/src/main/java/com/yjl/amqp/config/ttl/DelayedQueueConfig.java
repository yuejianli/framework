package com.yjl.amqp.config.ttl;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 延迟队列处理
 *
 * @author yuejianli
 * @date 2022-11-23
 */
@Component
public class DelayedQueueConfig {
    @Value("${rabbit.ttl.delayed_queue}")
    private String deadQueued;

    @Value("${rabbit.ttl.delayed_exchange}")
    private String deadExchange;

    @Value("${rabbit.ttl.delayed_routing_key}")
    private String deadRoutingKey;


    // 自定义交换机， 定义延迟交换机
    @Bean("delayedExchange")
    public CustomExchange delayedExchange() {
        Map<String, Object> args = new HashMap<>();
        // 自定义交换机的类型
        args.put("x-delayed-type", "direct");
        return new CustomExchange(deadExchange, "x-delayed-message", true, false, args);
    }

    @Bean("delayedQueue")
    public Queue delayedQueue() {
        return new Queue(deadQueued);
    }


    //进行绑定
    @Bean
    Binding bindingDelayedExchange(@Qualifier("delayedQueue") Queue queue,
                                   @Qualifier("delayedExchange") CustomExchange customExchange) {
        return BindingBuilder.bind(queue).to(customExchange).with(deadRoutingKey).noargs();
    }
}
