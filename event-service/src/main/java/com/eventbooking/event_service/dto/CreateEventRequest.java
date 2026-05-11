package com.eventbooking.event_service.dto;


public class CreateEventRequest {
    private String organizerId;
    private String title;
    private String date;
    private String venue;
    private int totalSeats;

    public CreateEventRequest(String organizerId, String title, String date, String venue, int totalSeats) {
        this.organizerId = organizerId;
        this.title = title;
        this.date = date;
        this.venue = venue;
        this.totalSeats = totalSeats;
    }

    public String getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }
}
