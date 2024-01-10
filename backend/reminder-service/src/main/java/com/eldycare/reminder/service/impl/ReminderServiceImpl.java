package com.eldycare.reminder.service.impl;

import com.eldycare.reminder.config.WebSocketReminderSender;
import com.eldycare.reminder.domain.Reminder;
import com.eldycare.reminder.dto.ReminderDto;
import com.eldycare.reminder.mapper.ReminderMapper;
import com.eldycare.reminder.repository.ReminderRepository;
import com.eldycare.reminder.service.service.ReminderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReminderServiceImpl implements ReminderService {

//    @Autowired
//    private ReminderRepository reminderRepository;

    @Autowired
    private WebSocketReminderSender webSocketReminderSender;
    @Autowired
    private ObjectMapper objectMapper;


    @Value("${amqp.queue}")
    private String reminderQueue;

    private transient final Logger logger = LoggerFactory.getLogger(ReminderServiceImpl.class);

    @Override
    public void sendReminder(ReminderDto reminderDto) {
        Reminder reminder = new Reminder(
                UUID.randomUUID().toString(),
                reminderDto.getElderEmail(),
                reminderDto.getRelativeEmail(),
                reminderDto.getDescription(),
                reminderDto.getReminderDate(),
                reminderDto.getReminderTime()
        );
        logger.info("sending this reminder to elder : {}", reminder);
        sendReminderToRelative(reminder);
    }

    // Method to send reminder to a specific elder
    private void sendReminderToRelative(Reminder reminder) {
        // Save the reminder to MongoDB
//        try{
//            logger.info("sendReminderToRelative - reminder: {}", reminder);
//            reminderRepository.save(reminder);
//        } catch (Exception e) {
//            logger.error("sendReminderToRelative - error: {}", e.getMessage());
//        }

        // Send a WebSocket message to the elder
        ReminderDto reminderDto = new ReminderDto(
                reminder.getElderEmail(),
                reminder.getRelativeEmail(),
                reminder.getReminderDate(),
                reminder.getReminderTime(),
                reminder.getDescription()
        );
        logger.info("sendReminderToRelative - reminderDto: {}", reminderDto);
        try{
            webSocketReminderSender.sendReminder(
                    reminder.getRelativeEmail(),
                    reminder.getElderEmail(),
                    objectMapper.writeValueAsString(reminderDto)
            );
        } catch (Exception e) {
            logger.error("sendReminderToRelative - error: {}", e.getMessage());
        }
    }
}
