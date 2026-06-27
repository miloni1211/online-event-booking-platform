package com.eventbooking.paymentservice.kafka;

import com.eventbooking.paymentservice.model.Payment;
import com.eventbooking.paymentservice.model.PaymentStatus;
import com.eventbooking.paymentservice.repository.PaymentRepository;
import com.stripe.model.Refund;
import com.stripe.param.RefundCreateParams;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.Optional;

@Component
public class BookingEventConsumer {

    private final PaymentRepository paymentRepository;
    private final PaymentEventProducer paymentEventProducer;
    private final ObjectMapper objectMapper;

    public BookingEventConsumer(
            PaymentRepository paymentRepository,
            PaymentEventProducer paymentEventProducer,
            ObjectMapper objectMapper) {
        this.paymentRepository = paymentRepository;
        this.paymentEventProducer = paymentEventProducer;
        this.objectMapper = objectMapper;
    }

    // WHY: Listen to booking-cancelled topic
    // When booking is cancelled:
    // → Check if payment exists
    // → If payment was successful → auto refund
    // → Seats already released by BookingService
    @KafkaListener(
            topics = "booking-cancelled",
            groupId = "payment-group"
    )
    public void handleBookingCancelled(String message) {
        try {
            System.out.println(
                    "Booking cancelled received " +
                            "in PaymentService: " + message);

            Map<String, Object> event = objectMapper
                    .readValue(message, Map.class);

            String bookingId =
                    (String) event.get("bookingId");
            String customerId =
                    (String) event.get("customerId");
            String eventId =
                    (String) event.get("eventId");

            // WHY: Check if payment exists
            // for this booking
            Optional<Payment> paymentOpt =
                    paymentRepository
                            .findByBookingId(bookingId);

            if (paymentOpt.isEmpty()) {
                // WHY: No payment found
                // booking was cancelled before payment
                // no refund needed
                System.out.println(
                        "No payment found for booking: "
                                + bookingId
                                + " - no refund needed");
                return;
            }

            Payment payment = paymentOpt.get();

            // WHY: Only refund if payment was successful
            // Cannot refund pending or failed payments
            if (payment.getStatus() !=
                    PaymentStatus.SUCCESS) {
                System.out.println(
                        "Payment status is "
                                + payment.getStatus()
                                + " - no refund needed");
                return;
            }

            // WHY: Auto trigger Stripe refund
            // Money returned to customer's card
            // Customer doesn't need to request manually
            RefundCreateParams params =
                    RefundCreateParams.builder()
                            .setPaymentIntent(
                                    payment.getStripePaymentIntentId())
                            .build();

            Refund.create(params);

            // WHY: Update payment status to REFUNDED
            payment.setStatus(PaymentStatus.REFUNDED);
            paymentRepository.save(payment);

            // WHY: Notify NotificationService
            // Customer gets refund notification
            paymentEventProducer.sendPaymentRefunded(
                    bookingId,
                    customerId,
                    eventId
            );

            System.out.println(
                    "Auto refund processed for booking: "
                            + bookingId);

        } catch (Exception e) {
            System.out.println(
                    "Error processing auto refund: "
                            + e.getMessage());
        }
    }
}