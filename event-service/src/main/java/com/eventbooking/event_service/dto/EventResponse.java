package com.eventbooking.event_service.dto;

import com.eventbooking.event_service.model.EventStatus;

public class EventResponse {

    private String organizerId;
    private String eventId;
    private String title;
    private String venue;
    private String date;
    private int availableSeats;
    private EventStatus eventStatus;

    public EventResponse(String organizerId, String eventId, String title, String venue, String date, int availableSeats, EventStatus eventStatus) {
        this.organizerId = organizerId;
        this.eventId = eventId;
        this.title = title;
        this.venue = venue;
        this.date = date;
        this.availableSeats = availableSeats;
        this.eventStatus = eventStatus;
    }

    public EventResponse() {
    }

    public String getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public EventStatus getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(EventStatus eventStatus) {
        this.eventStatus = eventStatus;
    }
}
