package com.eventbooking.ai_service.dto;

public class AiRequest {
    private String customerId;
    private String title;
    private String venue;
    private String date;
    private String category;
    private String query; // e.g. "outdoor music events this weekend"

    public AiRequest(String customerId, String title, String venue, String date, String category, String query) {
        this.customerId = customerId;
        this.title = title;
        this.venue = venue;
        this.date = date;
        this.category = category;
        this.query = query;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
