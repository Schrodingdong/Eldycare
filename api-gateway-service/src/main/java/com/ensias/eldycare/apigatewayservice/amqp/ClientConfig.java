package com.ensias.eldycare.apigatewayservice.amqp;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * This class is used to configure the RabbitMQ client for RPC pattern for JWT
 * validation
 */
@Configuration
public class ClientConfig {
    @Value("${amqp.auth.exchange}")
    private String exchangeName;

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(exchangeName);
    }

    /**
     * To ensure deserialization of Boolean class
     */
    @Bean
    public SimpleMessageConverter converter() {
        SimpleMessageConverter converter = new SimpleMessageConverter();
        converter.setAllowedListPatterns(List.of("java.util.*", "java.lang.*"));
        return converter;
    }
}
