package com.ensias.eldycare.apigatewayservice.amqp;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
public class SenderConfiguration {
    @Value("${amqp.auth.queue}")
    private String queueName;
    @Value("${amqp.auth.exchange}")
    private String exchangeName;
    private boolean isDurable = true;

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(exchangeName, isDurable, false);
    }

    @Bean
    public Queue queue(){
        return new Queue(queueName, isDurable);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange directExchange){
        return BindingBuilder
                .bind(queue)
                .to(directExchange)
                .with(queueName);
    }
}
