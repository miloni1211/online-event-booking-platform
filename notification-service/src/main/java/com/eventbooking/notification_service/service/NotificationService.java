package com.eventbooking.notification_service.service;

import com.eventbooking.notification_service.model.Notification;
import com.eventbooking.notification_service.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    // Alt+Insert → Constructor → select repository → OK
    public NotificationService(
            NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    // GET ALL NOTIFICATIONS FOR USER
    // WHY: Customer wants to see all their notifications
    public List<Notification> getNotificationsByUser(
            String userId) {
        return notificationRepository.findByUserId(userId);
    }

    // GET UNREAD NOTIFICATIONS
    // WHY: Show only unread notifications
    // like Gmail unread count
    public List<Notification> getUnreadNotifications(
            String userId) {
        return notificationRepository
                .findByUserIdAndIsRead(userId, false);
    }

    // MARK AS READ
    // WHY: When user reads notification
    // mark it as read
    public Notification markAsRead(String notificationId) {

        Notification notification = notificationRepository
                .findById(notificationId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Notification not found"));

        notification.setRead(true);
        return notificationRepository.save(notification);
    }
}