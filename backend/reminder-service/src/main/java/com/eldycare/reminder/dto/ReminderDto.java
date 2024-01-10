package com.eldycare.reminder.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class ReminderDto {
    private String elderEmail;
    private String relativeEmail;
    private LocalDate reminderDate;
    private LocalTime reminderTime;
    private String description;
}
