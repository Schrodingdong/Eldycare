package com.eldycare.notification.service.service;

import com.eldycare.notification.dto.NotificationDto;

public interface NotificationService {
//    void sendNotification(NotificationDto notification);
    void sendToElderTopic(NotificationDto notification);
}
