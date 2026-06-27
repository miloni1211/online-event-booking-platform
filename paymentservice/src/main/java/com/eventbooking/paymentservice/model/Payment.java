package com.eventbooking.paymentservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="payments")
public class Payment {
    @Id
    private String id;
    private String bookingId;
    private String customerId;
    private String eventId;
    private Long amount;

    // e.g. "usd", "inr", "gbp"
    private String currency;
    private PaymentStatus status;
    private String stripePaymentIntentId;
    private String createdAt;

    public Payment(String id, String bookingId, String customerId, String eventId, Long amount, String currency, PaymentStatus status, String stripePaymentIntentId, String createdAt) {
        this.id = id;
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.eventId = eventId;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.stripePaymentIntentId = stripePaymentIntentId;
        this.createdAt = createdAt;
    }

    public Payment() {

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

    @Override
    public String toString() {
        return "Payment{" +
                "id='" + id + '\'' +
                ", bookingId='" + bookingId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", eventId='" + eventId + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", status=" + status +
                ", stripePaymentIntentId='" + stripePaymentIntentId + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
