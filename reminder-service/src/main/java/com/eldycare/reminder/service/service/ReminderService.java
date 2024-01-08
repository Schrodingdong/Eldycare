package com.eldycare.reminder.service.service;

import com.eldycare.reminder.dto.ReminderDto;

public interface ReminderService {
    void sendReminder(ReminderDto reminderDto);
}
