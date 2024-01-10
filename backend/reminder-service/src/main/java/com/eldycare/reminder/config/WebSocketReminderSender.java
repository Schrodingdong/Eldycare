package com.eldycare.reminder.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WebSocketReminderSender {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    private transient final Logger logger = LoggerFactory.getLogger(WebSocketReminderSender.class);

    public void sendReminder(String relativeEmail, String elderEmail, String message) {
        // Send the message to the elder's reminder queue
        String destination = "/topic/reminder/" + elderEmail;
        logger.info("\n>>> Sending to : destination: {}", destination);
        try{
            // Include relative's email in the message payload
            byte[] byteMessage = message.getBytes();
            messagingTemplate.convertAndSend(destination, byteMessage);
            logger.info("\n>>> Message sent to elder: {}", elderEmail);
        } catch (Exception e) {
            logger.error("\n>>> Error sending message to elder: {}", elderEmail);
            e.printStackTrace();
        }
    }
}