package com.eldycare.notification.dto;

import com.eldycare.notification.domain.AlertType;
import lombok.Data;

import java.util.List;

@Data
public class NotificationDto {

    private String elderEmail;
    private String alertMessage;
    private List<AlertType> alertType;
    private String alertTime;
    private String location;
    private String relativeEmail;

    public  NotificationDto() { }

    public NotificationDto(String elderEmail, String alertMessage, List<AlertType> alertType, String alertTime, String location) {
        this.elderEmail = elderEmail;
        this.alertMessage = alertMessage;
        this.alertType = alertType;
        this.alertTime = alertTime;
        this.location = location;
    }

    public NotificationDto(String elderEmail, String relativeEmail, String alertMessage, List<AlertType> alertType, String alertTime, String location) {
        this.elderEmail = elderEmail;
        this.relativeEmail = relativeEmail;
        this.alertMessage = alertMessage;
        this.alertType = alertType;
        this.alertTime = alertTime;
        this.location = location;
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

    public String getAlertMessage() {
        return alertMessage;
    }

    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }

    public List<AlertType> getAlertType() {
        return alertType;
    }

    public void setAlertType(List<AlertType> alertType) {
        this.alertType = alertType;
    }

    public String getAlertTime() {
        return alertTime;
    }

    public void setAlertTime(String alertTime) {
        this.alertTime = alertTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}