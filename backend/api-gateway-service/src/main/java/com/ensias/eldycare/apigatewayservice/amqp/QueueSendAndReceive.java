package com.ensias.eldycare.apigatewayservice.amqp;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QueueSendAndReceive {
    @Autowired
    private DirectExchange directExchange;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public Message sendAndReceive(String message){
        Message m = new Message(message.getBytes());
        return rabbitTemplate.sendAndReceive(directExchange.getName(), m);
    }
}
