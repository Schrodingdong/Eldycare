package com.eldycare.notification.service.service;

import com.eldycare.notification.dto.NotificationDto;

public interface NotificationService {
    public void sendNotification(NotificationDto notification);
}
