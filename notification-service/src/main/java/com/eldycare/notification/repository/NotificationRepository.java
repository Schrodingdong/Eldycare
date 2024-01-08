package com.eldycare.notification.repository;

import com.eldycare.notification.domain.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

public interface NotificationRepository extends MongoRepository<Notification, String> {
}
