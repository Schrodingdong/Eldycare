package com.eldycare.reminder.service.impl;

import com.eldycare.reminder.config.WebSocketReminderSender;
import com.eldycare.reminder.domain.Reminder;
import com.eldycare.reminder.dto.ReminderDto;
import com.eldycare.reminder.mapper.ReminderMapper;
import com.eldycare.reminder.repository.ReminderRepository;
import com.eldycare.reminder.service.service.ReminderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ReminderServiceImpl implements ReminderService {

    @Autowired
    private ReminderRepository reminderRepository;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private WebSocketReminderSender webSocketReminderSender;

    private ReminderMapper reminderMapper;

    @Value("${amqp.queue}")
    private String reminderQueue;

    private transient final Logger logger = LoggerFactory.getLogger(ReminderServiceImpl.class);

    @Override
    public void sendReminder(ReminderDto reminderDto) {

        String elderEmail = reminderDto.getElderEmail();
        Reminder reminder = reminderMapper.toReminderEntity(reminderDto);
        logger.info("sending this reminder to elder : {}", reminder);
        sendReminderToRelative(reminder);
    }

    // Method to send reminder to a specific elder
    private void sendReminderToRelative(Reminder reminder) {
        // Save the reminder to MongoDB
        logger.info("sendReminderToRelative - reminder: {}", reminder);
        reminderRepository.save(reminder);

        // Send a WebSocket message to the elder
        webSocketReminderSender.sendReminder(
                reminder.getRelativeEmail(),
                reminder.getElderEmail(),
                reminder.getDescription()
        );
    }
}
