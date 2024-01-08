package com.eldycare.notification.service.impl;

import com.eldycare.notification.config.WebSocketNotificationSender;
import com.eldycare.notification.domain.Notification;
import com.eldycare.notification.dto.NotificationDto;
import com.eldycare.notification.mapper.NotificationMapper;
import com.eldycare.notification.repository.NotificationRepository;
import com.eldycare.notification.service.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private WebSocketNotificationSender webSocketNotificationSender;

    private transient final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Override
    public void sendToElderTopic(NotificationDto notification){
        logger.info("sendToElderTopic - notification: {}", notification);
        webSocketNotificationSender.sendNotification(notification.getElderEmail(), notification);
    }

}
