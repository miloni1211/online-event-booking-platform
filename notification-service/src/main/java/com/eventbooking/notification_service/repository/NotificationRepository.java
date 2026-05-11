package com.eventbooking.notification_service.repository;

import com.eventbooking.notification_service.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository
        extends MongoRepository<Notification, String> {

    // WHY: Customer wants to see all their notifications
    List<Notification> findByUserId(String userId);

    // WHY: Get only unread notifications
    // so we can show unread count like Gmail
    List<Notification> findByUserIdAndIsRead(
            String userId,
            boolean isRead);
}