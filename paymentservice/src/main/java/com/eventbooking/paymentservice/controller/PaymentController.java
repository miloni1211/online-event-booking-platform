package com.eventbooking.paymentservice.controller;

import com.eventbooking.paymentservice.dto.PaymentRequest;
import com.eventbooking.paymentservice.dto.PaymentResponse;
import com.eventbooking.paymentservice.dto.RefundRequest;
import com.eventbooking.paymentservice.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(
            PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // CREATE PAYMENT INTENT
    // WHY: First step of payment
    // Creates intent in Stripe
    // Returns clientSecret to frontend
    @PostMapping("/create")
    public ResponseEntity<PaymentResponse> createPayment(
            @RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService
                .createPaymentIntent(request);
        return ResponseEntity.ok(response);
    }

    // CONFIRM PAYMENT
    // WHY: Called after customer completes payment
    // Updates payment status
    @PostMapping("/confirm/{paymentIntentId}")
    public ResponseEntity<PaymentResponse> confirmPayment(
            @PathVariable String paymentIntentId) {
        PaymentResponse response = paymentService
                .confirmPayment(paymentIntentId);
        return ResponseEntity.ok(response);
    }

    // REFUND PAYMENT
    // WHY: Called when booking is cancelled
    // Triggers automatic Stripe refund
    @PostMapping("/refund")
    public ResponseEntity<PaymentResponse> refundPayment(
            @RequestBody RefundRequest request) {
        PaymentResponse response = paymentService
                .refundPayment(request);
        return ResponseEntity.ok(response);
    }

    // GET PAYMENT BY BOOKING ID
    // WHY: Check payment status for a booking
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<PaymentResponse> getPaymentByBooking(
            @PathVariable String bookingId) {
        PaymentResponse response = paymentService
                .getPaymentByBookingId(bookingId);
        return ResponseEntity.ok(response);
    }

    // GET PAYMENT HISTORY BY CUSTOMER
    // WHY: Customer views all their payments
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByCustomer(
            @PathVariable String customerId) {
        List<PaymentResponse> responses = paymentService
                .getPaymentsByCustomer(customerId);
        return ResponseEntity.ok(responses);
    }
}