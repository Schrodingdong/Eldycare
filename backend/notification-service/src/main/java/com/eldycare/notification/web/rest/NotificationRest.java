package com.eldycare.notification.web.rest;

import com.eldycare.notification.config.WebSocketNotificationSender;
import com.eldycare.notification.constants.ServiceConstants;
import com.eldycare.notification.dto.NotificationDto;
import com.eldycare.notification.service.service.NotificationService;
import com.eldycare.notification.utils.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
public class NotificationRest {

    @Autowired
    NotificationService notificationService;
    @Autowired
    WebSocketNotificationSender webSocketNotificationSender;

    @PostMapping("/send")
    public ResponseEntity<?> sendNotification(@RequestBody NotificationDto notification) {
        try {
            notificationService.sendToElderTopic(notification);
            return SystemUtils.getResponseEntity(ServiceConstants.NOTIFICATION_SENT_SUCCESSFULLY, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return SystemUtils.getResponseEntity(ServiceConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
