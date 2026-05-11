package com.eventbooking.event_service.repository;

import com.eventbooking.event_service.model.Event;
import com.eventbooking.event_service.model.EventStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {

List<Event> findByOrganizerId(String organizerId);

List<Event> findByEventStatus(EventStatus eventStatus);

List<Event> findByTitleContains(String keyword);


}
