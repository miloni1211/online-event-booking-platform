package com.eventbooking.bookingservice.kafka;

public class BookingEvent {
    // WHY: What type of event happened
    // BOOKING_CONFIRMED or BOOKING_CANCELLED
    private String eventType;

    // WHY: Which booking this is about
    private String bookingId;

    // WHY: Which customer to notify
    private String customerId;

    // WHY: Which event was booked
    private String eventId;

    // WHY: How many seats
    private int seatsBooked;

    public BookingEvent(String eventType, String bookingId, String customerId, String eventId, int seatsBooked) {
        this.eventType = eventType;
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.eventId = eventId;
        this.seatsBooked = seatsBooked;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
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

    public int getSeatsBooked() {
        return seatsBooked;
    }

    public void setSeatsBooked(int seatsBooked) {
        this.seatsBooked = seatsBooked;
    }

    @Override
    public String toString() {
        return "BookingEvent{" +
                "eventType='" + eventType + '\'' +
                ", bookingId='" + bookingId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", eventId='" + eventId + '\'' +
                ", seatsBooked=" + seatsBooked +
                '}';
    }
}
