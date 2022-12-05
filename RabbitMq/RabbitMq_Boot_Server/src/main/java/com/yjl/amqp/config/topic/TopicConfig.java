package com.yjl.amqp.config.topic;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Topic 形式的 生产
 *
 * @author yuejianli
 * @date 2022-11-22
 */
@Component
public class TopicConfig {

    @Value("${rabbit.topic.queue1}")
    private String queue1;

    @Value("${rabbit.topic.exchange}")
    private String exchange;


    // 构建队列 Bean 和 Exchange Bean
    @Bean(value = "topic_queue1")
    public Queue queue1() {
        return new Queue(queue1);
    }

    @Bean(value = "topic_exchange")
    TopicExchange topicExchange() {
        return new TopicExchange(exchange);
    }


    //进行绑定
    @Bean
    Binding bindingTopicExchange(@Qualifier("topic_queue1") Queue queue,
                                 @Qualifier("topic_exchange") TopicExchange topicExchange) {
        return BindingBuilder.bind(queue).to(topicExchange)
                .with("*.orange.*");
    }

}
