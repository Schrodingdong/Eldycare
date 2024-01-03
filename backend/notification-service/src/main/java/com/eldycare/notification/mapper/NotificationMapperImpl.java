package com.eldycare.notification.mapper;


import com.eldycare.notification.domain.Notification;
import com.eldycare.notification.dto.NotificationDto;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapperImpl implements NotificationMapper{
    @Override
    public Notification toNotification(NotificationDto notificationDto) {
        if(notificationDto == null)
            return null;
        Notification notification = new Notification();
        notification.setElderEmail(notificationDto.getElderEmail());
        notification.setAlertMessage(notificationDto.getAlertMessage());
        notification.setAlertType(notificationDto.getAlertType());
        notification.setAlertTime(notificationDto.getAlertTime());
        notification.setLocation(notificationDto.getLocation());
        notification.setRelativeEmail(notificationDto.getRelativeEmail());
        return notification;
    }

    @Override
    public NotificationDto toNotificationDto(Notification notification) {
        if(notification == null)
            return null;
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setElderEmail(notification.getElderEmail());
        notificationDto.setAlertMessage(notification.getAlertMessage());
        notificationDto.setAlertType(notification.getAlertType());
        notificationDto.setAlertTime(notification.getAlertTime());
        notificationDto.setLocation(notification.getLocation());
        notificationDto.setRelativeEmail(notification.getRelativeEmail());
        return notificationDto;
    }
}
