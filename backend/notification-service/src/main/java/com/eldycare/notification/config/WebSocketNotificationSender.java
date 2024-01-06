package com.eldycare.notification.config;

import com.eldycare.notification.service.impl.NotificationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WebSocketNotificationSender {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    private transient final Logger logger = LoggerFactory.getLogger(WebSocketNotificationSender.class);

    public void sendNotification(String elderEmail, String message) {
        // Send the message to the relative's notification queue
        String destination = "/topic/alert/" + elderEmail;
        logger.info("\n>>> Sending to : destination: {}", destination);
        try{
            messagingTemplate.convertAndSend(destination, message);
            logger.info("\n>>> Message sent !");
        } catch (Exception e) {
            logger.info("\n>>> Message not sent !");
            e.printStackTrace();
        }
    }
}