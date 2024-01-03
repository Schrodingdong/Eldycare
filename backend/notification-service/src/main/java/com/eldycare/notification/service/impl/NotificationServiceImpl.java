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
    private NotificationRepository notificationRepository;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private WebSocketNotificationSender webSocketNotificationSender;

    @Autowired
    private NotificationMapper notificationMapper;

    @Value("${amqp.notif.queue}")
    private String notificationQueue;

    private transient final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Override
    public void sendNotification(NotificationDto notificationDto) {

        String elderEmail = notificationDto.getElderEmail();
        List<String> closeRelativeEmails = getCloseRelativeIds(elderEmail);
        logger.info("closeRelativeEmails: {}", closeRelativeEmails);

        // Create a CountDownLatch to synchronize the main thread with the worker threads
        CountDownLatch latch = new CountDownLatch(closeRelativeEmails.size());

        // Create a fixed-size thread pool
        ExecutorService executorService;
        if (!closeRelativeEmails.isEmpty()) {
            executorService = Executors.newFixedThreadPool(closeRelativeEmails.size());
        } else {
            // Handle the case where closeRelativeIds is empty
            executorService = Executors.newFixedThreadPool(1);
        }

        // Create a list to store the tasks
        List<Runnable> tasks = new ArrayList<>();

        // Send notification for all close relatives using multithreading
        for (String closeRelativeId : closeRelativeEmails) {
            tasks.add(() -> {
                // TODO : Modify this part to include the convertAndSend function
                notificationDto.setRelativeEmail(closeRelativeId);
                Notification notification = notificationMapper.toNotification(notificationDto);
                logger.info("sending this notification to relative : {}", notification);
                sendNotificationToRelative(notification);

                // Notify the CountDownLatch that a thread has finished
                latch.countDown();
            });
        }

        // Submit all tasks
        for (Runnable task : tasks) {
            executorService.submit(task);
        }

        // Shutdown the executor to prevent new tasks from being submitted
        executorService.shutdown();

        try {
            // Wait for all threads to finish
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            // Handle interruption
        }
    }

    // Method to send notification to a specific relative
    private void sendNotificationToRelative(Notification notification) {
        // Save the notification to MongoDB
        logger.info("sendNotificatiionTORelative - notification: {}", notification);
        // TODO : problem in saving
//        Notification savedNotif = notificationRepository.save(notification);
//        logger.info("savedNotif: {}", savedNotif);

        // Send a WebSocket message to the relative
        webSocketNotificationSender.sendNotification(
                notification.getElderEmail(),
                notification.getAlertMessage()
        );
    }

    private List<String> getCloseRelativeIds(String elderEmail) {
        // Send request to user service
        logger.info("getCloseRelativeIds: {}", elderEmail);
        Object response = amqpTemplate.convertSendAndReceive(notificationQueue, elderEmail);
        logger.info("getCloseRelativeIds response: {}", response);

        // Process the response
        if (response instanceof List<?>) {
            // Cast the response to List<String>
            List<String> closeRelativeEmails = (List<String>) response;
            return closeRelativeEmails;
        } else {
            // Handle the case when the reply is not of the expected type
            logger.info("Unexpected response type: {}", response.getClass());
            return Collections.emptyList(); // Or handle it differently based on your requirements
        }
    }

}
