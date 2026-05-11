package com.eventbooking.event_service.controller;


import com.eventbooking.event_service.dto.CreateEventRequest;
import com.eventbooking.event_service.dto.EventResponse;
import com.eventbooking.event_service.dto.UpdateEventRequest;
import com.eventbooking.event_service.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/create")
    public ResponseEntity<EventResponse> createEvent(@RequestBody CreateEventRequest request){
        EventResponse response = eventService.createEvent(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    ResponseEntity<EventResponse> updateEvent(@PathVariable String id, @RequestBody UpdateEventRequest request){
        EventResponse response=eventService.updateEvent(id,request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/cancel/{id}")
    public ResponseEntity<EventResponse> cancelEvent(@PathVariable String id){
        EventResponse response=eventService.cancelEvent(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable String id){
        EventResponse response=eventService.getEventById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/active")
    public ResponseEntity<List<EventResponse>> getActiveEvents(){
        List<EventResponse> responses=eventService.getActiveEvents();

        return ResponseEntity.ok(responses);
    }


    @GetMapping("/organizer/{organizerId}")
    public ResponseEntity<List<EventResponse>> getEventsByOrganizerId(@PathVariable String organizerId){
        List<EventResponse> responses=eventService.getEventsByOrganizerId(organizerId);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/searchByTitle")
    public ResponseEntity<List<EventResponse>> getByTitleContains(@RequestParam String keyword){
        List<EventResponse> responses=eventService.getByTitleContains(keyword);

        return ResponseEntity.ok(responses);
    }


}
