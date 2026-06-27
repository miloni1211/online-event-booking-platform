package com.eventbooking.paymentservice.dto;

import com.eventbooking.paymentservice.model.PaymentStatus;

public class PaymentResponse {
    private String id;
    private String bookingId;
    private String customerId;
    private String eventId;
    private Long amount;
    private String currency;
    private PaymentStatus status;
    // WHY: Client secret is needed by frontend
    // to complete payment using Stripe.js
    // Never expose server secret key to frontend
    private String clientSecret;

    private String stripePaymentIntentId;
    private String createdAt;

    public PaymentResponse(String id, String bookingId, String customerId, String eventId, Long amount, String currency, PaymentStatus status, String clientSecret, String stripePaymentIntentId, String createdAt) {
        this.id = id;
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.eventId = eventId;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.clientSecret = clientSecret;
        this.stripePaymentIntentId = stripePaymentIntentId;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getStripePaymentIntentId() {
        return stripePaymentIntentId;
    }

    public void setStripePaymentIntentId(String stripePaymentIntentId) {
        this.stripePaymentIntentId = stripePaymentIntentId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
