package com.eldycare.reminder.web.rest;

import com.eldycare.reminder.constants.ServiceConstants;
import com.eldycare.reminder.dto.ReminderDto;
import com.eldycare.reminder.service.service.ReminderService;
import com.eldycare.reminder.utils.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reminder")
public class ReminderRest {

    @Autowired
    ReminderService reminderService;

    @PostMapping("/send")
    public ResponseEntity<?> sendReminder(@RequestBody ReminderDto reminderDto) {
        try {
            reminderService.sendReminder(reminderDto);
            return SystemUtils.getResponseEntity(ServiceConstants.REMINDER_SENT_SUCCESSFULLY, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return SystemUtils.getResponseEntity(ServiceConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
