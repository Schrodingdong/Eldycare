package com.eldycare.reminder.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReminderDto {

    private String elderEmail;
    private String relativeEmail;
    private String description;
    private LocalDateTime reminderDateTime;

    public  ReminderDto() { }

    public ReminderDto(String elderEmail, String description, LocalDateTime reminderDateTime) {
        this.elderEmail = elderEmail;
        this.description = description;
        this.reminderDateTime = reminderDateTime;
    }

    public ReminderDto(String elderEmail, String relativeEmail, String description, LocalDateTime reminderDateTime) {
        this.elderEmail = elderEmail;
        this.relativeEmail = relativeEmail;
        this.description = description;
        this.reminderDateTime = reminderDateTime;
    }

    public String getElderEmail() {
        return elderEmail;
    }

    public void setElderEmail(String elderEmail) {
        this.elderEmail = elderEmail;
    }

    public String getRelativeEmail() {
        return relativeEmail;
    }

    public void setRelativeEmail(String relativeEmail) {
        this.relativeEmail = relativeEmail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getReminderDateTime() {
        return reminderDateTime;
    }

    public void setReminderDateTime(LocalDateTime reminderDateTime) {
        this.reminderDateTime = reminderDateTime;
    }
}
