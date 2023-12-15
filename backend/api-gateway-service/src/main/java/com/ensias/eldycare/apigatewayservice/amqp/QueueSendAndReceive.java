package com.ensias.eldycare.apigatewayservice.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger LOG = LoggerFactory.getLogger(QueueSendAndReceive.class);

    public Message sendAndReceive(String message){
        Message m = new Message(message.getBytes());
        LOG.info("Sending message to JWT validation queue: " + message);
        return rabbitTemplate.sendAndReceive(directExchange.getName(), m);
    }
}
