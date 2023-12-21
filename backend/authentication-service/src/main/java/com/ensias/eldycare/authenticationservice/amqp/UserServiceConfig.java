package com.ensias.eldycare.authenticationservice.amqp;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
public class UserServiceConfig {
    @Value("${amqp.user.queue}")
    private String queueName;
    @Value("${amqp.user.exchange}")
    private String exchangeName;

    @Bean("userQueue")
    public Queue userQueue() {
        return new Queue(queueName);
    }

    @Bean("userExchange")
    public DirectExchange userExchange() {
        return new DirectExchange(exchangeName);
    }

    @Bean
    public Binding userBinding(Queue userQueue, DirectExchange userExchange) {
        return BindingBuilder
                .bind(userQueue)
                .to(userExchange)
                .with(exchangeName);
    }

}
