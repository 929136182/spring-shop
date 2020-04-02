package com.yzp.springshopmailservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.yzp.common.constant.RabbitConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Bean
    public Queue getQueue(){
        return new Queue(RabbitConstant.PRODUCT_MAIL_QUEUE,true,false,false);
    }

    @Bean
    public TopicExchange getTopicExchange(){
        return new TopicExchange(RabbitConstant.PRODUCT_MAIL_EXCHANGE);
    }

    @Bean
    public Binding getBinding(Queue getQueue, TopicExchange getTopicExchange){
        return BindingBuilder.bind(getQueue).to(getTopicExchange).with("product.add");
    }
}
