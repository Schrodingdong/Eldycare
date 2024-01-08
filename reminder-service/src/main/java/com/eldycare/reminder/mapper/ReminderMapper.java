package com.eldycare.reminder.mapper;

import com.eldycare.reminder.domain.Reminder;
import com.eldycare.reminder.dto.ReminderDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReminderMapper extends EntityMapper<ReminderDto, Reminder> {

    Reminder toReminder(ReminderDto reminderDto);

    ReminderDto toReminderDto(Reminder reminder);
}