package com.eventbooking.bookingservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "bookings")
public class Booking {

    @Id
    private String id;

    // WHY: Which event is being booked
    private String eventId;

    // WHY: Which customer is booking
    private String customerId;

    // WHY: How many seats customer wants
    private int seatsBooked;

    // WHY: Total cost at time of booking
    // stored here because price might change later
    private BookingStatus status;

    // WHY: When was this booking made
    private String bookedAt;

    public Booking(String id, String eventId, String customerId, int seatsBooked, BookingStatus status, String bookedAt) {
        this.id = id;
        this.eventId = eventId;
        this.customerId = customerId;
        this.seatsBooked = seatsBooked;
        this.status = status;
        this.bookedAt = bookedAt;
    }

    public Booking() {

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
