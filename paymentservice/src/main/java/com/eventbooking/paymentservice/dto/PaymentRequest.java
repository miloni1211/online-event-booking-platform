package com.eventbooking.paymentservice.dto;

public class PaymentRequest {
    // WHY: Which booking this payment is for
    private String bookingId;

    // WHY: Who is paying
    private String customerId;

    // WHY: Which event
    private String eventId;

    // WHY: Amount in cents
    // e.g. 1000 = $10.00
    private Long amount;

    // WHY: Currency code
    // e.g. "usd"
    private String currency;

    public PaymentRequest(String bookingId, String customerId, String eventId, Long amount, String currency) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.eventId = eventId;
        this.amount = amount;
        this.currency = currency;
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
}
