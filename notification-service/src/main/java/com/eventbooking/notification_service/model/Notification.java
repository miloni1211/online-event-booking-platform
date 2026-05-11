package com.eventbooking.notification_service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notifications")
public class Notification {

    @Id
    private String id;

    // WHY: Which user to notify
    private String userId;

    // WHY: Which event this is about
    private String eventId;

    // WHY: What kind of notification
    private NotificationType type;

    // WHY: Human readable message
    private String message;

    // WHY: When notification was created
    private String createdAt;

    // WHY: Has user seen this notification?
    private boolean isRead;

    // Alt+Insert → Constructor → select all → OK
    // Alt+Insert → Getter and Setter → select all → OK
    // Alt+Insert → toString → select all → OK

    public Notification(String id, String userId, String eventId, NotificationType type, String message, String createdAt, boolean isRead) {
        this.id = id;
        this.userId = userId;
        this.eventId = eventId;
        this.type = type;
        this.message = message;
        this.createdAt = createdAt;
        this.isRead = isRead;
    }

    public Notification() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", eventId='" + eventId + '\'' +
                ", type=" + type +
                ", message='" + message + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", isRead=" + isRead +
                '}';
    }
}