package com.eventbooking.bookingservice.dto;

public class BookingRequest {
    private String eventId;
    private String customerId;
    private int seatsBooked;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public int getSeatsBooked() {
        return seatsBooked;
    }

    public void setSeatsBooked(int seatsBooked) {
        this.seatsBooked = seatsBooked;
    }

    public BookingRequest(String eventId, String customerId, int seatsBooked) {
        this.eventId = eventId;
        this.customerId = customerId;
        this.seatsBooked = seatsBooked;
    }
}
