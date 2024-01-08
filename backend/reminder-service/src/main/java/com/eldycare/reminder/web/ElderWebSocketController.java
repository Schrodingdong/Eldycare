package com.eldycare.reminder.web;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ElderWebSocketController {

    private transient final Logger logger = LoggerFactory.getLogger(ElderWebSocketController.class);

    @MessageMapping("/reminder/{elderEmail}")
    @SendTo("/topic/reminder/{elderEmail}")
    public String handleReminder(String message) {
        // Handle the reminder message received from relatives
        logger.info("\n>>> Received reminder: {}", message);
        return message;
    }
}