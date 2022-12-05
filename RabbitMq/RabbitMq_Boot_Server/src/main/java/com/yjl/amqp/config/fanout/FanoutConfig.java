package com.yjl.amqp.config.fanout;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Fanout 形式的 生产
 *
 * @author yuejianli
 * @date 2022-11-22
 */
@Component
public class FanoutConfig {

    @Value("${rabbit.fanout.queue1}")
    private String queue1;
    @Value("${rabbit.fanout.queue2}")
    private String queue2;

    @Value("${rabbit.fanout.exchange}")
    private String exchange;


    // 构建队列 Bean 和 Exchange Bean
    @Bean(value = "fanout_queue1")
    public Queue queue1() {
        return new Queue(queue1);
    }

    @Bean(value = "fanout_queue2")
    public Queue queue2() {
        return new Queue(queue2);
    }

    @Bean(value = "fanout_exchange")
    FanoutExchange fanoutExchange() {
        return new FanoutExchange(exchange);
    }


    //进行绑定
    @Bean
    Binding bindingFanoutExchange1(@Qualifier("fanout_queue1") Queue queue,
                                   @Qualifier("fanout_exchange") FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(queue).to(fanoutExchange);
    }

    @Bean
    Binding bindingFanoutExchange2(@Qualifier("fanout_queue2") Queue queue,
                                   @Qualifier("fanout_exchange") FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(queue).to(fanoutExchange);
    }

}
