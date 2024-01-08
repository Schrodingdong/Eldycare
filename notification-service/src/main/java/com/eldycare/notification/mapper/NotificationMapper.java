package com.eldycare.notification.mapper;

import com.eldycare.notification.domain.Notification;
import com.eldycare.notification.dto.NotificationDto;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

/**
 *
 * @author Yassine Ouhadi
 *
 */

@Mapper(componentModel = "spring")
//public interface NotificationMapper extends EntityMapper<NotificationDto, Notification> {
public interface NotificationMapper {
    Notification toNotification(NotificationDto notificationDto);
    NotificationDto toNotificationDto(Notification notification);
}