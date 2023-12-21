package com.eldycare.notification.config;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WebSocketNotificationSender {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketNotificationSender(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendNotification(String relativeEmail, String message) {
        String destination = "/topic/notifications/" + relativeEmail;
        messagingTemplate.convertAndSend(destination, message);
    }
}