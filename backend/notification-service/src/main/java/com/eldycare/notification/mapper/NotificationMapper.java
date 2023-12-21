package com.eldycare.notification.mapper;

import com.eldycare.notification.domain.Notification;
import com.eldycare.notification.dto.NotificationDto;
import org.mapstruct.Mapper;

/**
 *
 * @author Yassine Ouhadi
 *
 */

@Mapper(componentModel = "spring")
public interface NotificationMapper extends EntityMapper<NotificationDto, Notification> {
    Notification toNotification(NotificationDto notificationDto);

    NotificationDto toNotificationDto(Notification notification);
}