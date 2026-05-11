package com.eventbooking.event_service.service;

import com.eventbooking.event_service.dto.CreateEventRequest;
import com.eventbooking.event_service.dto.EventResponse;
import com.eventbooking.event_service.dto.UpdateEventRequest;
import com.eventbooking.event_service.model.Event;
import com.eventbooking.event_service.model.EventStatus;
import com.eventbooking.event_service.repository.EventRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public EventResponse createEvent(CreateEventRequest request){
        Event event=new Event();
        event.setOrganizerId(request.getOrganizerId());
        event.setTitle(request.getTitle());
        event.setDate(request.getDate());
        event.setVenue(request.getVenue());
        event.setTotalSeats(request.getTotalSeats());
        event.setAvailableSeats(request.getTotalSeats());
        event.setEventStatus(EventStatus.ACTIVE);

        Event savedEvent=eventRepository.save(event);

        return mapToResponse(savedEvent);

    }

    @CacheEvict(value = {"events", "activeEvents"},
            allEntries = true)
    public EventResponse updateEvent(String eventId,UpdateEventRequest request){

        System.out.println("Updating event, clearing cache: " + eventId);
        Event event=eventRepository.findById(eventId).orElseThrow(()->new RuntimeException("Event Not found !"));

        event.setTitle(request.getTitle());
        event.setVenue(request.getVenue());
        event.setDate(request.getDate());
        event.setTotalSeats(request.getTotalSeats());
        event.setAvailableSeats(request.getTotalSeats());

        Event savedEvent=eventRepository.save(event);

        return mapToResponse(savedEvent);

    }

    @CacheEvict(value = {"events", "activeEvents"},
            allEntries = true)
    public EventResponse cancelEvent(String eventId){
        System.out.println("Cancelling event, clearing cache: " + eventId);
        Event event=eventRepository.findById(eventId).orElseThrow(()->new RuntimeException("Event Not found !"));
        if(event.getEventStatus()==EventStatus.CANCELLED)
        {
            throw new RuntimeException("Event already canceled");
        }

        event.setEventStatus(EventStatus.CANCELLED);
        Event savedEvent=eventRepository.save(event);
        return mapToResponse(savedEvent);
    }

    @Cacheable(value = "events", key = "#eventId")
    public EventResponse getEventById(String eventId){
        System.out.println("Fetching event from MongoDB: " + eventId);
        Event event=eventRepository.findById(eventId).orElseThrow(()->new RuntimeException("Event Not found !"));

        return mapToResponse(event);
    }

    public List<EventResponse> getEventsByOrganizerId(String organizerId){
        return eventRepository.findByOrganizerId(organizerId).stream().map(this::mapToResponse).collect(Collectors.toList());

    }

    @Cacheable(value = "activeEvents")
    public List<EventResponse> getActiveEvents(){
        System.out.println("Fetching active events from MongoDB...");
        return eventRepository.findByEventStatus(EventStatus.ACTIVE).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<EventResponse> getByTitleContains(String keyword){

        return eventRepository.findByTitleContains(keyword).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private EventResponse mapToResponse(Event savedEvent) {
        return new EventResponse(
                savedEvent.getOrganizerId(),
                savedEvent.getId(),
                savedEvent.getTitle(),
        savedEvent.getVenue(),
        savedEvent.getDate(),
        savedEvent.getAvailableSeats(),
        savedEvent.getEventStatus()
                );

    }



}
