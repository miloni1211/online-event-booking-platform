package com.eventbooking.paymentservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class PaymentEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public PaymentEventProducer(
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    // WHY: Notify BookingService and NotificationService
    // payment succeeded → confirm booking
    public void sendPaymentConfirmed(
            String bookingId,
            String customerId,
            String eventId,
            Long amount) {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("bookingId", bookingId);
            event.put("customerId", customerId);
            event.put("eventId", eventId);
            event.put("amount", amount);
            event.put("eventType", "PAYMENT_CONFIRMED");

            String message = objectMapper
                    .writeValueAsString(event);

            // WHY: BookingService listens to this
            // to confirm booking
            kafkaTemplate.send(
                    "payment-confirmed", message);

            System.out.println(
                    "Payment confirmed event sent: "
                            + message);

        } catch (Exception e) {
            System.out.println(
                    "Error sending payment confirmed: "
                            + e.getMessage());
        }
    }

    // WHY: Notify BookingService
    // payment failed → release seats → cancel booking
    public void sendPaymentFailed(
            String bookingId,
            String customerId,
            String eventId) {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("bookingId", bookingId);
            event.put("customerId", customerId);
            event.put("eventId", eventId);
            event.put("eventType", "PAYMENT_FAILED");

            String message = objectMapper
                    .writeValueAsString(event);

            // WHY: BookingService listens to this
            // to release seats and cancel booking
            kafkaTemplate.send(
                    "payment-failed", message);

            System.out.println(
                    "Payment failed event sent: "
                            + message);

        } catch (Exception e) {
            System.out.println(
                    "Error sending payment failed: "
                            + e.getMessage());
        }
    }

    // WHY: Notify NotificationService
    // payment refunded → save notification
    public void sendPaymentRefunded(
            String bookingId,
            String customerId,
            String eventId) {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("bookingId", bookingId);
            event.put("customerId", customerId);
            event.put("eventId", eventId);
            event.put("eventType", "PAYMENT_REFUNDED");

            String message = objectMapper
                    .writeValueAsString(event);

            kafkaTemplate.send(
                    "payment-refunded", message);

            System.out.println(
                    "Payment refunded event sent: "
                            + message);

        } catch (Exception e) {
            System.out.println(
                    "Error sending payment refunded: "
                            + e.getMessage());
        }
    }
}