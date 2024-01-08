package com.ensias.eldycare.authenticationservice.amqp;

import com.ensias.eldycare.authenticationservice.amqp.model.UserModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserServiceAmqpSaveUser {
    @Autowired
    private DirectExchange userExchange;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    private final Logger LOG = LoggerFactory.getLogger(UserServiceAmqpSaveUser.class);
    @Autowired
    private ObjectMapper objectMapper;

    public void saveUser(UserModel user) {
        LOG.info("Sending user to User-Service : " + user);
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("email", user.getEmail());
        objectNode.put("phone", user.getPhone());
        objectNode.put("username", user.getUsername());
        objectNode.put("userType", user.getUserType());
        rabbitTemplate.convertAndSend(userExchange.getName(), userExchange.getName(), objectNode.toString());
    }
}
