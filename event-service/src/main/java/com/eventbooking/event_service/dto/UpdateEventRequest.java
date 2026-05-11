package com.eventbooking.event_service.dto;


public class UpdateEventRequest {

    private String title;
    private String date;
    private String venue;
    private int totalSeats;

    public UpdateEventRequest(String title, String date, String venue, int totalSeats) {
        this.title = title;
        this.date = date;
        this.venue = venue;
        this.totalSeats = totalSeats;
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
