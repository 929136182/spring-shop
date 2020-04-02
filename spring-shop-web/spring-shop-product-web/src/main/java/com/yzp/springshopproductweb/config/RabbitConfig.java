package com.yzp.springshopproductweb.config;

import com.yzp.common.constant.RabbitConstant;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public TopicExchange getProductTopicExchange(){
        return new TopicExchange(RabbitConstant.PRODUCT_EXCHANGE);
    }

    @Bean
    public TopicExchange getMailTopicExchange(){
        return new TopicExchange(RabbitConstant.PRODUCT_MAIL_EXCHANGE);
    }
}
