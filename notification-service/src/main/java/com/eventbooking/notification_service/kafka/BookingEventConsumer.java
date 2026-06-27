package com.eventbooking.notification_service.kafka;

import com.eventbooking.notification_service.model.Notification;
import com.eventbooking.notification_service.model.NotificationType;
import com.eventbooking.notification_service.repository.NotificationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.Map;

@Component
public class BookingEventConsumer {

    private final NotificationRepository notificationRepository;
    private final ObjectMapper objectMapper;

    // Alt+Insert → Constructor → select both → OK
    public BookingEventConsumer(
            NotificationRepository notificationRepository,
            ObjectMapper objectMapper) {
        this.notificationRepository = notificationRepository;
        this.objectMapper = objectMapper;
    }



    // LISTEN TO BOOKING CONFIRMED TOPIC
    // WHY: @KafkaListener automatically triggers
    // this method when message arrives on topic
    @KafkaListener(
            topics = "booking-confirmed",
            groupId = "notification-group"
    )
    public void handleBookingConfirmed(String message) {
        try {
            System.out.println(
                    "Received booking confirmed: " + message);

            // WHY: Convert JSON string back to Map
            // to extract fields
            Map<String, Object> event = objectMapper
                    .readValue(message, Map.class);

            // WHY: Build notification object
            Notification notification = new Notification();
            notification.setUserId(
                    (String) event.get("customerId"));
            notification.setEventId(
                    (String) event.get("eventId"));
            notification.setType(
                    NotificationType.BOOKING_CONFIRMED);
            notification.setMessage(
                    "Your booking is confirmed! " +
                            "Seats booked: " +
                            event.get("seatsBooked"));
            notification.setCreatedAt(
                    LocalDateTime.now().toString());
            notification.setRead(false);

            // WHY: Save notification to MongoDB
            notificationRepository.save(notification);

            System.out.println("Notification saved: "
                    + notification);

        } catch (Exception e) {
            System.out.println(
                    "Error processing booking confirmed: "
                            + e.getMessage());
        }
    }

    // WHY: Notify customer payment confirmed
    @KafkaListener(
            topics = "payment-confirmed",
            groupId = "notification-group"
    )
    public void handlePaymentConfirmed(String message) {
        try {
            Map<String, Object> event = objectMapper
                    .readValue(message, Map.class);

            Notification notification = new Notification();
            notification.setUserId(
                    (String) event.get("customerId"));
            notification.setEventId(
                    (String) event.get("eventId"));
            notification.setType(
                    NotificationType.BOOKING_CONFIRMED);
            notification.setMessage(
                    "Payment successful! Your booking " +
                            "is confirmed. Amount paid: $" +
                            ((Number) event.get("amount")).longValue() / 100);
            notification.setCreatedAt(
                    LocalDateTime.now().toString());
            notification.setRead(false);

            notificationRepository.save(notification);

        } catch (Exception e) {
            System.out.println(
                    "Error handling payment confirmed notification: "
                            + e.getMessage());
        }
    }

    // WHY: Notify customer payment failed
    @KafkaListener(
            topics = "payment-failed",
            groupId = "notification-group"
    )
    public void handlePaymentFailed(String message) {
        try {
            Map<String, Object> event = objectMapper
                    .readValue(message, Map.class);

            Notification notification = new Notification();
            notification.setUserId(
                    (String) event.get("customerId"));
            notification.setEventId(
                    (String) event.get("eventId"));
            notification.setType(
                    NotificationType.BOOKING_CANCELLED);
            notification.setMessage(
                    "Payment failed. Your booking has " +
                            "been cancelled and seats released.");
            notification.setCreatedAt(
                    LocalDateTime.now().toString());
            notification.setRead(false);

            notificationRepository.save(notification);

        } catch (Exception e) {
            System.out.println(
                    "Error handling payment failed notification: "
                            + e.getMessage());
        }
    }

    // WHY: Notify customer payment refunded
    @KafkaListener(
            topics = "payment-refunded",
            groupId = "notification-group"
    )
    public void handlePaymentRefunded(String message) {
        try {
            Map<String, Object> event = objectMapper
                    .readValue(message, Map.class);

            Notification notification = new Notification();
            notification.setUserId(
                    (String) event.get("customerId"));
            notification.setEventId(
                    (String) event.get("eventId"));
            notification.setType(
                    NotificationType.BOOKING_CANCELLED);
            notification.setMessage(
                    "Your payment has been refunded. " +
                            "Amount will reflect in 3-5 business days.");
            notification.setCreatedAt(
                    LocalDateTime.now().toString());
            notification.setRead(false);

            notificationRepository.save(notification);

        } catch (Exception e) {
            System.out.println(
                    "Error handling payment refunded notification: "
                            + e.getMessage());
        }
    }

    // LISTEN TO BOOKING CANCELLED TOPIC
    @KafkaListener(
            topics = "booking-cancelled",
            groupId = "notification-group"
    )
    public void handleBookingCancelled(String message) {
        try {
            System.out.println(
                    "Received booking cancelled: " + message);

            Map<String, Object> event = objectMapper
                    .readValue(message, Map.class);

            Notification notification = new Notification();
            notification.setUserId(
                    (String) event.get("customerId"));
            notification.setEventId(
                    (String) event.get("eventId"));
            notification.setType(
                    NotificationType.BOOKING_CANCELLED);
            notification.setMessage(
                    "Your booking has been cancelled. " +
                            "Seats released: " +
                            event.get("seatsBooked"));
            notification.setCreatedAt(
                    LocalDateTime.now().toString());
            notification.setRead(false);

            notificationRepository.save(notification);

            System.out.println("Notification saved: "
                    + notification);

        } catch (Exception e) {
            System.out.println(
                    "Error processing booking cancelled: "
                            + e.getMessage());
        }
    }
}