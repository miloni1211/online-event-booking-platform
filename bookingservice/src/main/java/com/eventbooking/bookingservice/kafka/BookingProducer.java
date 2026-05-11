package com.eventbooking.bookingservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class BookingProducer {

    // WHY: KafkaTemplate is Spring's tool
    // for sending messages to Kafka topics
    private final KafkaTemplate<String, String> kafkaTemplate;

    // WHY: ObjectMapper converts Java object to JSON string
    // Kafka sends messages as strings
    private final ObjectMapper objectMapper;

    // Alt+Insert → Constructor → select both → OK
    public BookingProducer(
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    // SEND BOOKING CONFIRMED
    // WHY: Called after successful booking
    // Notifies other services booking happened
    public void sendBookingConfirmed(BookingEvent event) {
        try {
            // WHY: Convert BookingEvent object to JSON string
            // Kafka can only send strings/bytes
            String message = objectMapper
                    .writeValueAsString(event);

            // WHY: Send to "booking-confirmed" topic
            // Notification service listens to this topic
            kafkaTemplate.send("booking-confirmed", message);

            System.out.println(
                    "Booking confirmed event sent: " + message);

        } catch (Exception e) {
            System.out.println(
                    "Error sending booking confirmed event: "
                            + e.getMessage());
        }
    }

    // SEND BOOKING CANCELLED
    // WHY: Called after booking cancellation
    public void sendBookingCancelled(BookingEvent event) {
        try {
            String message = objectMapper
                    .writeValueAsString(event);

            // WHY: Send to "booking-cancelled" topic
            kafkaTemplate.send("booking-cancelled", message);

            System.out.println(
                    "Booking cancelled event sent: " + message);

        } catch (Exception e) {
            System.out.println(
                    "Error sending booking cancelled event: "
                            + e.getMessage());
        }
    }
}