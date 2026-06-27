package com.eventbooking.paymentservice.dto;

public class RefundRequest {
    // WHY: Which booking to refund
    private String bookingId;

    // WHY: Why refund is being issued
    private String reason;

    public RefundRequest(String bookingId, String reason) {
        this.bookingId = bookingId;
        this.reason = reason;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
