package com.eldycare.reminder.repository;

import com.eldycare.reminder.domain.Reminder;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReminderRepository extends MongoRepository<Reminder, String> {
}
