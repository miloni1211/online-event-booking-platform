package com.eventbooking.bookingservice.dto;

import com.eventbooking.bookingservice.model.BookingStatus;

public class BookingResponse {

    private String id;
    private String eventId;
    private String customerId;
    private int seatsBooked;
    private BookingStatus status;
    private String bookedAt;

    public BookingResponse(String id, String eventId, String customerId, int seatsBooked, BookingStatus status, String bookedAt) {
        this.id = id;
        this.eventId = eventId;
        this.customerId = customerId;
        this.seatsBooked = seatsBooked;
        this.status = status;
        this.bookedAt = bookedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public String getBookedAt() {
        return bookedAt;
    }

    public void setBookedAt(String bookedAt) {
        this.bookedAt = bookedAt;
    }
}
