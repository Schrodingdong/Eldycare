package com.ensias.eldycare.userservice.amqp;

import com.ensias.eldycare.userservice.model.UserModel;
import com.ensias.eldycare.userservice.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class RegisterUserAmqpListener {

    @Autowired
    private AmqpTemplate amqpTemplate;
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

    @RabbitListener(queues = "${amqp.notif.queue}")
    public void registerNotification(Message message) {
        String elderEmailWithQuotes = new String(message.getBody());
        String elderEmail = elderEmailWithQuotes.substring(1, elderEmailWithQuotes.length() - 1);
        // Process the elderEmail and retrieve the list of close relatives
        List<String> closeRelativeEmails = getCloseRelativeEmails(elderEmail);

        // Create a response message and set the correlation ID
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setCorrelationId(message.getMessageProperties().getCorrelationId());
        Message responseMessage = new Message(convertListToJson(closeRelativeEmails).getBytes(), messageProperties);

        // Send the response back to the notification service
        amqpTemplate.send(message.getMessageProperties().getReplyTo(), responseMessage);
    }

    private List<String> getCloseRelativeEmails(String elderEmail) {
        Set<UserModel> relatives = userService.getUrgentContacts(elderEmail);
        List<String> relativeEmails = new ArrayList<>();

        for (UserModel relative : relatives) {
            relativeEmails.add(relative.getEmail());
        }

        return relativeEmails;
    }

    private String convertListToJson(List<String> list) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(list);
        } catch (Exception e) {
            // Handle exception (e.g., log it)
            return "";
        }
    }
}
