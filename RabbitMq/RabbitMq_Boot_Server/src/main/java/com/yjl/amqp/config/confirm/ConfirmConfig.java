package com.yjl.amqp.config.confirm;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 确认配置
 *
 * @author yuejianli
 * @date 2022-11-24
 */
@Component
public class ConfirmConfig {

    @Value("${rabbit.confirm.queue}")
    private String queue;

    @Value("${rabbit.confirm.exchange}")
    private String exchange;

    @Value("${rabbit.confirm.routing-key}")
    private String routingKey;


    // 配置未达的那些
    @Value("${rabbit.confirm.backup_queue}")
    private String backupQueue;

    @Value("${rabbit.confirm.backup_exchange}")
    private String backupExchange;

    @Value("${rabbit.confirm.warn_queue}")
    private String warnQueue;


//    @Bean(value="confirm_direct_exchange")
//    DirectExchange confirmDirectExchange(){
//        return new DirectExchange(exchange);
//    }


    @Bean(value = "confirm_direct_exchange")
    DirectExchange confirmDirectExchange() {
        ExchangeBuilder exchangeBuilder = ExchangeBuilder.directExchange(exchange)
                .durable(true)
                //设置备份
                .withArgument("alternate-exchange", backupExchange);
        return exchangeBuilder.build();
    }


    @Bean(value = "confirm_queue")
    public Queue confirmQueue() {
        return new Queue(queue);
    }

    //进行绑定
    @Bean
    Binding confirmBindingTopicExchange(@Qualifier("confirm_queue") Queue queue,
                                        @Qualifier("confirm_direct_exchange") DirectExchange directExchange) {
        return BindingBuilder.bind(queue).to(directExchange).with(routingKey);
    }


    @Bean(value = "backup_confirm_direct_exchange")
    FanoutExchange backupConfirmDirectExchange() {
        return new FanoutExchange(backupExchange);
    }

    @Bean(value = "backup_confirm_queue")
    public Queue backupConfirmQueue() {
        return new Queue(backupQueue);
    }

    @Bean(value = "warn_confirm_queue")
    public Queue warnConfirmQueue() {
        return new Queue(warnQueue);
    }


    //进行绑定
    @Bean
    Binding backupConfirmBindingTopicExchange(@Qualifier("backup_confirm_queue") Queue queue,
                                              @Qualifier("backup_confirm_direct_exchange") FanoutExchange directExchange) {
        return BindingBuilder.bind(queue).to(directExchange);
    }

    //进行绑定
    @Bean
    Binding warnConfirmBindingTopicExchange(@Qualifier("warn_confirm_queue") Queue queue,
                                            @Qualifier("backup_confirm_direct_exchange") FanoutExchange directExchange) {
        return BindingBuilder.bind(queue).to(directExchange);
    }
}
