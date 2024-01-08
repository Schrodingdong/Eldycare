package com.eldycare.reminder.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
/**
 * Represents a reminder document.
 */
@Document(collection = "reminders")
@Data
public class Reminder {

    @Id
    private String id;

    @Field("elderEmail")
    private String elderEmail;

    @Field("relativeEmail")
    private String relativeEmail;

    @Field("description")
    private String description;

    @Field("reminderDateTime")
    private LocalDateTime reminderDateTime;

    public Reminder() { }

    public Reminder(String elderEmail, String relativeEmail, String description, LocalDateTime reminderDateTime) {
        this.elderEmail = elderEmail;
        this.relativeEmail = relativeEmail;
        this.description = description;
        this.reminderDateTime = reminderDateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Reminder{" +
                "id='" + id + '\'' +
                ", elderEmail='" + elderEmail + '\'' +
                ", relativeEmail='" + relativeEmail + '\'' +
                ", description='" + description + '\'' +
                ", reminderDateTime=" + reminderDateTime +
                '}';
    }
}
