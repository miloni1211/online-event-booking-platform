package com.eventbooking.bookingservice.kafka;

import com.eventbooking.bookingservice.model.BookingStatus;
import com.eventbooking.bookingservice.repository.BookingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class PaymentEventConsumer {

    private final BookingRepository bookingRepository;
    private final MongoTemplate mongoTemplate;
    private final ObjectMapper objectMapper;

    public PaymentEventConsumer(
            BookingRepository bookingRepository,
            MongoTemplate mongoTemplate,
            ObjectMapper objectMapper) {
        this.bookingRepository = bookingRepository;
        this.mongoTemplate = mongoTemplate;
        this.objectMapper = objectMapper;
    }

    // WHY: Listen to payment-confirmed topic
    // Update booking to CONFIRMED
    @KafkaListener(
            topics = "payment-confirmed",
            groupId = "booking-group"
    )
    public void handlePaymentConfirmed(String message) {
        try {
            System.out.println(
                    "Payment confirmed received: " + message);

            Map<String, Object> event = objectMapper
                    .readValue(message, Map.class);

            String bookingId =
                    (String) event.get("bookingId");

            // WHY: Update booking status to CONFIRMED
            // Payment successful → booking is valid
            bookingRepository.findById(bookingId)
                    .ifPresent(booking -> {
                        booking.setStatus(
                                BookingStatus.CONFIRMED);
                        bookingRepository.save(booking);
                        System.out.println(
                                "Booking confirmed: "
                                        + bookingId);
                    });

        } catch (Exception e) {
            System.out.println(
                    "Error handling payment confirmed: "
                            + e.getMessage());
        }
    }

    // WHY: Listen to payment-failed topic
    // Release seats + cancel booking
    @KafkaListener(
            topics = "payment-failed",
            groupId = "booking-group"
    )
    public void handlePaymentFailed(String message) {
        try {
            System.out.println(
                    "Payment failed received: " + message);

            Map<String, Object> event = objectMapper
                    .readValue(message, Map.class);

            String bookingId =
                    (String) event.get("bookingId");

            // WHY: Find booking and release seats
            bookingRepository.findById(bookingId)
                    .ifPresent(booking -> {

                        // Release seats back to event
                        Query query = new Query(
                                Criteria.where("_id")
                                        .is(booking.getEventId())
                        );
                        Update update = new Update()
                                .inc("availableSeats",
                                        booking.getSeatsBooked());
                        mongoTemplate.updateFirst(
                                query, update, "events");

                        // Cancel the booking
                        booking.setStatus(
                                BookingStatus.CANCELLED);
                        bookingRepository.save(booking);

                        System.out.println(
                                "Booking cancelled, "
                                        + "seats released: "
                                        + bookingId);
                    });

        } catch (Exception e) {
            System.out.println(
                    "Error handling payment failed: "
                            + e.getMessage());
        }
    }
}