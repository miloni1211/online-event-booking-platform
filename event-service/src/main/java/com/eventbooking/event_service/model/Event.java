package com.eventbooking.event_service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "events")
public class Event {
    @Id
    private String id;
    private String organizerId;
    private String title;
    private String date;
    private String venue;
    private int totalSeats;
    private int availableSeats;
    private EventStatus eventStatus;

    @Version
    private Long version;

    public Event(String id, String organizerId, String title, String date, String venue, int totalSeats, int availableSeats, EventStatus eventStatus, Long version) {
        this.id = id;
        this.organizerId = organizerId;
        this.title = title;
        this.date = date;
        this.venue = venue;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.eventStatus = eventStatus;
        this.version = version;
    }

    public Event() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", organizerId='" + organizerId + '\'' +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", venue='" + venue + '\'' +
                ", totalSeats=" + totalSeats +
                ", availableSeats=" + availableSeats +
                ", eventStatus=" + eventStatus +
                ", version=" + version +
                '}';
    }
}
