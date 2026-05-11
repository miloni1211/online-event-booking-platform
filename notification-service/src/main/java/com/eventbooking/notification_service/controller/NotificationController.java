package com.eventbooking.notification_service.controller;

import com.eventbooking.notification_service.model.Notification;
import com.eventbooking.notification_service.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    // Alt+Insert → Constructor → select service → OK
    public NotificationController(
            NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // GET ALL NOTIFICATIONS FOR USER
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getNotificationsByUser(
            @PathVariable String userId) {
        List<Notification> notifications = notificationService
                .getNotificationsByUser(userId);
        return ResponseEntity.ok(notifications);
    }

    // GET UNREAD NOTIFICATIONS
    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications(
            @PathVariable String userId) {
        List<Notification> notifications = notificationService
                .getUnreadNotifications(userId);
        return ResponseEntity.ok(notifications);
    }

    // MARK NOTIFICATION AS READ
    @PatchMapping("/{id}/read")
    public ResponseEntity<Notification> markAsRead(
            @PathVariable String id) {
        Notification notification = notificationService
                .markAsRead(id);
        return ResponseEntity.ok(notification);
    }
}