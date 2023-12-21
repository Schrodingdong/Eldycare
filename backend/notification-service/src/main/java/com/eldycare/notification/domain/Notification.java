package com.eldycare.notification.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

/**
 * Represents a notification document.
 */
@Document(collection = "notifications")  // Specify the MongoDB collection name
@Data
public class Notification {

    @Id
    private String id;  // Automatically generated MongoDB identifier

    @Field("elderEmail")
    private String elderEmail;

    @Field("relativeEmail")
    private String relativeEmail;

    @Field("alertMessage")
    private String alertMessage;

    @Field("alertType")
    private List<AlertType> alertType;

    @Field("alertTime")
    private String alertTime;

    @Field("location")
    private String location;

    public Notification() { }

    public Notification(String elderEmail, String relativeEmail, String alertMessage, List<AlertType> alertType, String alertTime, String location) {
        this.elderEmail = elderEmail;
        this.relativeEmail = relativeEmail;
        this.alertMessage = alertMessage;
        this.alertType = alertType;
        this.alertTime = alertTime;
        this.location = location;
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

    @Override
    public String toString() {
        return "Notification{" +
                "id='" + id + '\'' +
                ", elderId='" + elderEmail + '\'' +
                ", relativeId='" + relativeEmail + '\'' +
                ", alertMessage='" + alertMessage + '\'' +
                ", alertType=" + alertType +
                ", alertTime='" + alertTime + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
