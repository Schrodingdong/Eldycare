package com.eldycare.reminder.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Represents a reminder document.
 */
@Document(collection = "reminders")
@Data
@AllArgsConstructor
@ToString
public class Reminder {

    @Id
    private String id;

    @Field("elderEmail")
    private String elderEmail;

    @Field("relativeEmail")
    private String relativeEmail;

    @Field("description")
    private String description;

    @Field("reminderDate")
    private LocalDate reminderDate;

    @Field("reminderTime")
    private LocalTime reminderTime;

}
