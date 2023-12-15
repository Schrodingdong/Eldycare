package com.ensias.eldycare.userservice.amqp;

import com.ensias.eldycare.userservice.model.UserModel;
import com.ensias.eldycare.userservice.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegisterUserAmqpListener {
    @Autowired
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;
    private Logger LOG = LoggerFactory.getLogger(RegisterUserAmqpListener.class);

    @RabbitListener(queues = "${amqp.user.queue}")
    public void registerUser(String jsonToParse) throws JsonProcessingException {
        UserModel user = objectMapper.readValue(jsonToParse, UserModel.class);
        LOG.info("Received user from Auth-Service : " + user);
        userService.addUser(user);
    }
}
