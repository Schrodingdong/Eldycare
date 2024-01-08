package com.eldycare.notification.config;

import com.eldycare.notification.dto.NotificationDto;
import com.eldycare.notification.service.impl.NotificationServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WebSocketNotificationSender {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    private transient final Logger logger = LoggerFactory.getLogger(WebSocketNotificationSender.class);

    public void sendNotification(String elderEmail, NotificationDto notification) {
        // Send the message to the relative's notification queue
        String destination = "/topic/alert/" + elderEmail;
        logger.info("\n>>> Sending to : destination: {}", destination);
        try{
            byte[] byteMessage = objectMapper.writeValueAsBytes(notification);
            messagingTemplate.convertAndSend(destination, byteMessage);
            logger.info("\n>>> Message sent !");
        } catch (Exception e) {
            logger.info("\n>>> Message not sent !");
            e.printStackTrace();
        }
    }
}