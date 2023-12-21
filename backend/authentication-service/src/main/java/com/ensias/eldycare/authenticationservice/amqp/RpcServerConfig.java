package com.ensias.eldycare.authenticationservice.amqp;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * This class is used to configure the RabbitMQ server for RPC pattern for JWT
 * validation
 */
@Component
public class RpcServerConfig {
    @Value("${amqp.auth.queue}")
    private String queueName;
    @Value("${amqp.auth.exchange}")
    private String exchangeName;

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(exchangeName);
    }

    @Bean
    public Queue queue() {
        return new Queue(queueName);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange directExchange) {
        return BindingBuilder
                .bind(queue)
                .to(directExchange)
                .with("rpc");
    }
}
