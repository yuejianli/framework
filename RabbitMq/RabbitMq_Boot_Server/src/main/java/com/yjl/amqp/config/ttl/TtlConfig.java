package com.yjl.amqp.config.ttl;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 列信队列配置
 *
 * @author yuejianli
 * @date 2022-11-23
 */
@Component
public class TtlConfig {
    @Value("${rabbit.ttl.queue_a}")
    private String queuea;
    @Value("${rabbit.ttl.queue_b}")
    private String queueb;

    @Value("${rabbit.ttl.x_exchange}")
    private String xexchange;


    @Value("${rabbit.ttl.y_dead_queue_d}")
    private String deadQueued;

    @Value("${rabbit.ttl.y_dead_exchange}")
    private String ydeadExchange;


    @Value("${rabbit.ttl.queue_c}")
    private String queuec;


    // 定义队列和 交换机，并进行绑定


    @Bean(value = "direct_exchange")
    DirectExchange directExchange() {
        return new DirectExchange(xexchange);
    }


    @Bean(value = "direct_queuea")
    public Queue queuea() {
        //return new Queue(queuea);

        Map<String, Object> args = new HashMap<>();

        args.put("x-dead-letter-exchange", ydeadExchange);

        args.put("x-dead-letter-routing-key", "YD");

        // 声明 ttl  10s
        args.put("x-message-ttl", 10000);

        return QueueBuilder.durable(queuea).withArguments(args).build();
    }


    //进行绑定
    @Bean
    Binding bindingDirectExchangea(@Qualifier("direct_queuea") Queue queue,
                                   @Qualifier("direct_exchange") DirectExchange directExchange) {
        return BindingBuilder.bind(queue).to(directExchange).with("XA");
    }


    @Bean(value = "direct_queueb")
    public Queue queueb() {
        // return new Queue(queueb);

        Map<String, Object> args = new HashMap<>();

        args.put("x-dead-letter-exchange", ydeadExchange);

        args.put("x-dead-letter-routing-key", "YD");

        // 声明 ttl  40s
        args.put("x-message-ttl", 40000);

        return QueueBuilder.durable(queueb).withArguments(args).build();
    }


    @Bean
    Binding bindingDirectExchangeb(@Qualifier("direct_queueb") Queue queue,
                                   @Qualifier("direct_exchange") DirectExchange directExchange) {
        return BindingBuilder.bind(queue).to(directExchange).with("XB");
    }


    @Bean(value = "direct_dead_exchange")
    DirectExchange directDeadExchange() {
        return new DirectExchange(ydeadExchange);
    }


    @Bean(value = "direct_dead_queued")
    public Queue deadQueued() {
        return new Queue(deadQueued);
    }

    // 进行绑定
    @Bean
    Binding bindingDeadDirectExchange(@Qualifier("direct_dead_queued") Queue queue,
                                      @Qualifier("direct_dead_exchange") DirectExchange directExchange) {
        return BindingBuilder.bind(queue).to(directExchange).with("YD");
    }


    // 声明一个队列 C, 没有过期时间的。


    @Bean(value = "direct_queuec")
    public Queue queuec() {
        // return new Queue(queueb);

        Map<String, Object> args = new HashMap<>();

        args.put("x-dead-letter-exchange", ydeadExchange);

        args.put("x-dead-letter-routing-key", "YD");


        return QueueBuilder.durable(queuec).withArguments(args).build();
    }


    @Bean
    Binding bindingDirectExchangec(@Qualifier("direct_queuec") Queue queue,
                                   @Qualifier("direct_exchange") DirectExchange directExchange) {
        return BindingBuilder.bind(queue).to(directExchange).with("XC");
    }


}
