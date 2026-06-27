package com.eventbooking.paymentservice.service;

import com.eventbooking.paymentservice.dto.PaymentRequest;
import com.eventbooking.paymentservice.dto.PaymentResponse;
import com.eventbooking.paymentservice.dto.RefundRequest;
import com.eventbooking.paymentservice.kafka.PaymentEventProducer;
import com.eventbooking.paymentservice.model.Payment;
import com.eventbooking.paymentservice.model.PaymentStatus;
import com.eventbooking.paymentservice.repository.PaymentRepository;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.RefundCreateParams;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentEventProducer paymentEventProducer;

    // Alt+Insert → Constructor → select both → OK
    public PaymentService(
            PaymentRepository paymentRepository,
            PaymentEventProducer paymentEventProducer) {
        this.paymentRepository = paymentRepository;
        this.paymentEventProducer = paymentEventProducer;
    }

    // CREATE PAYMENT INTENT
    // WHY: First step of payment process
    // Creates payment intent in Stripe
    // Returns clientSecret to frontend
    // No money charged yet at this point
    public PaymentResponse createPaymentIntent(
            PaymentRequest request) {
        try {

            // WHY: Build Stripe payment intent params
            PaymentIntentCreateParams params =
                    PaymentIntentCreateParams.builder()
                            .setAmount(request.getAmount())
                            .setCurrency(request.getCurrency())

                            // WHY: Disable automatic payment methods
                            // that require redirects (like bank transfers)
                            // We only want card payments for now
                            .setAutomaticPaymentMethods(
                                    PaymentIntentCreateParams
                                            .AutomaticPaymentMethods
                                            .builder()
                                            .setEnabled(true)
                                            // WHY: Prevents redirect-based
                                            // payment methods
                                            // Cards don't need redirects
                                            .setAllowRedirects(
                                                    PaymentIntentCreateParams
                                                            .AutomaticPaymentMethods
                                                            .AllowRedirects.NEVER)
                                            .build()
                            )

                            // WHY: Auto confirm with test Visa card
                            .setConfirm(true)
                            .setPaymentMethod("pm_card_visa")

                            .putMetadata("bookingId",
                                    request.getBookingId())
                            .putMetadata("customerId",
                                    request.getCustomerId())
                            .build();

            // WHY: Create payment intent in Stripe
            // registers payment intention
            // no money charged at this point
            PaymentIntent paymentIntent =
                    PaymentIntent.create(params);

            // WHY: Save payment record to MongoDB
            // track payment status on our side too
            Payment payment = new Payment();
            payment.setBookingId(request.getBookingId());
            payment.setCustomerId(request.getCustomerId());
            payment.setEventId(request.getEventId());
            payment.setAmount(request.getAmount());
            payment.setCurrency(request.getCurrency());
            // WHY: PENDING because customer hasn't paid yet
            payment.setStatus(PaymentStatus.PENDING);
            payment.setStripePaymentIntentId(
                    paymentIntent.getId());
            payment.setCreatedAt(
                    LocalDateTime.now().toString());

            Payment savedPayment =
                    paymentRepository.save(payment);

            System.out.println(
                    "Payment intent created: "
                            + paymentIntent.getId());

            // WHY: Return clientSecret to frontend
            // frontend needs this to show payment form
            // and complete payment via Stripe.js
            return mapToResponse(
                    savedPayment,
                    paymentIntent.getClientSecret());

        } catch (Exception e) {
            throw new RuntimeException(
                    "Payment creation failed: "
                            + e.getMessage());
        }
    }

    // CONFIRM PAYMENT
    // WHY: Called after customer completes payment
    // Checks Stripe for actual payment status
    // Updates our MongoDB record
    // Notifies BookingService via Kafka
    public PaymentResponse confirmPayment(
            String paymentIntentId) {
        try {

            // WHY: Ask Stripe for current status
            // source of truth is always Stripe
            PaymentIntent paymentIntent =
                    PaymentIntent.retrieve(paymentIntentId);

            // WHY: Find our payment record in MongoDB
            Payment payment = paymentRepository
                    .findAll()
                    .stream()
                    .filter(p -> p
                            .getStripePaymentIntentId()
                            .equals(paymentIntentId))
                    .findFirst()
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Payment not found"));

            // WHY: Check what Stripe says
            if ("succeeded".equals(
                    paymentIntent.getStatus())) {

                // Payment successful
                payment.setStatus(PaymentStatus.SUCCESS);
                Payment updatedPayment =
                        paymentRepository.save(payment);

                // WHY: Publish to Kafka
                // BookingService will confirm booking
                // NotificationService will notify customer
                paymentEventProducer.sendPaymentConfirmed(
                        payment.getBookingId(),
                        payment.getCustomerId(),
                        payment.getEventId(),
                        payment.getAmount()
                );

                System.out.println(
                        "Payment confirmed: "
                                + paymentIntentId);

                return mapToResponse(
                        updatedPayment, null);

            } else {

                // Payment failed
                payment.setStatus(PaymentStatus.FAILED);
                Payment updatedPayment =
                        paymentRepository.save(payment);

                // WHY: Publish to Kafka
                // BookingService will release seats
                // and cancel booking
                paymentEventProducer.sendPaymentFailed(
                        payment.getBookingId(),
                        payment.getCustomerId(),
                        payment.getEventId()
                );

                System.out.println(
                        "Payment failed: "
                                + paymentIntentId);

                return mapToResponse(
                        updatedPayment, null);
            }

        } catch (Exception e) {
            throw new RuntimeException(
                    "Payment confirmation failed: "
                            + e.getMessage());
        }
    }

    // REFUND PAYMENT
    // WHY: Called when booking is cancelled
    // after successful payment
    // Triggers automatic Stripe refund
    // Money returned to customer's card
    public PaymentResponse refundPayment(
            RefundRequest request) {
        try {

            // WHY: Find payment by booking ID
            Payment payment = paymentRepository
                    .findByBookingId(request.getBookingId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Payment not found for booking: "
                                            + request.getBookingId()));

            // WHY: Only refund successful payments
            // Cannot refund pending or failed payments
            if (payment.getStatus() !=
                    PaymentStatus.SUCCESS) {
                throw new RuntimeException(
                        "Only successful payments " +
                                "can be refunded. Current status: "
                                + payment.getStatus());
            }

            // WHY: Tell Stripe to process refund
            // Stripe handles returning money to card
            // Usually takes 3-5 business days
            RefundCreateParams params =
                    RefundCreateParams.builder()
                            .setPaymentIntent(
                                    payment.getStripePaymentIntentId())
                            .build();

            Refund.create(params);

            // WHY: Update our record to REFUNDED
            payment.setStatus(PaymentStatus.REFUNDED);
            Payment updatedPayment =
                    paymentRepository.save(payment);

            // WHY: Publish to Kafka
            // NotificationService will notify customer
            // "Your refund has been processed"
            paymentEventProducer.sendPaymentRefunded(
                    payment.getBookingId(),
                    payment.getCustomerId(),
                    payment.getEventId()
            );

            System.out.println(
                    "Payment refunded for booking: "
                            + request.getBookingId());

            return mapToResponse(updatedPayment, null);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Refund failed: " + e.getMessage());
        }
    }

    // GET PAYMENT BY BOOKING ID
    // WHY: Check payment status for specific booking
    // Useful for customer to verify payment
    public PaymentResponse getPaymentByBookingId(
            String bookingId) {

        Payment payment = paymentRepository
                .findByBookingId(bookingId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Payment not found for booking: "
                                        + bookingId));

        return mapToResponse(payment, null);
    }

    // GET PAYMENT HISTORY BY CUSTOMER
    // WHY: Customer views all their past payments
    // Shows payment status for each booking
    public List<PaymentResponse> getPaymentsByCustomer(
            String customerId) {

        return paymentRepository
                .findByCustomerId(customerId)
                .stream()
                .map(p -> mapToResponse(p, null))
                .collect(Collectors.toList());
    }

    // PRIVATE HELPER
    // WHY: Converts Payment model to PaymentResponse DTO
    // clientSecret only returned on creation
    // null for all other operations
    // (security - don't expose secret unnecessarily)
    private PaymentResponse mapToResponse(
            Payment payment,
            String clientSecret) {
        return new PaymentResponse(
                payment.getId(),
                payment.getBookingId(),
                payment.getCustomerId(),
                payment.getEventId(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getStatus(),
                clientSecret,
                payment.getStripePaymentIntentId(),
                payment.getCreatedAt()
        );
    }
}