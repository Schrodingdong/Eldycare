package com.eldycare.reminder.web.rest;

import com.eldycare.reminder.constants.ServiceConstants;
import com.eldycare.reminder.dto.ReminderDto;
import com.eldycare.reminder.service.service.ReminderService;
import com.eldycare.reminder.utils.SystemUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
    private ReminderService reminderService;
    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/send")
    public ResponseEntity<?> sendReminder(@RequestBody ReminderDto reminderDto) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        try {
            reminderService.sendReminder(reminderDto);
            objectNode.put("message", ServiceConstants.REMINDER_SENT_SUCCESSFULLY);
            return ResponseEntity.ok().body(objectNode);
        } catch (Exception e) {
            e.printStackTrace();
            objectNode.put("message", ServiceConstants.SOMETHING_WENT_WRONG);
            return SystemUtils.getResponseEntity(ServiceConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
