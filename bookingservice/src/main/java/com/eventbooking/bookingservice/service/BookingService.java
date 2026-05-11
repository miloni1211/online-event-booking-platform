package com.eventbooking.bookingservice.service;

import com.eventbooking.bookingservice.dto.BookingRequest;
import com.eventbooking.bookingservice.dto.BookingResponse;
import com.eventbooking.bookingservice.kafka.BookingEvent;
import com.eventbooking.bookingservice.kafka.BookingProducer;
import com.eventbooking.bookingservice.model.Booking;
import com.eventbooking.bookingservice.model.BookingStatus;
import com.eventbooking.bookingservice.repository.BookingRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final MongoTemplate mongoTemplate;
    private final BookingProducer bookingProducer; // ← add this


    public BookingService(BookingRepository bookingRepository, MongoTemplate mongoTemplate, BookingProducer bookingProducer) {
        this.bookingRepository = bookingRepository;
        this.mongoTemplate = mongoTemplate;
        this.bookingProducer = bookingProducer;
    }

    public BookingResponse bookTicket(BookingRequest request) {

        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        // WHY: credentials holds userId we stored in JwtFilter
        String loggedInUserId = (String) auth.getCredentials();

        // WHY: Verify customerId in request matches
        // the actual logged in user
        // Prevents booking on behalf of someone else
        if (!loggedInUserId.equals(request.getCustomerId())) {
            throw new RuntimeException(
                    "You can only book tickets for yourself");
        }

        // WHY: Get event details directly from MongoDB
        // instead of calling Event Service via REST
        Query eventQuery = new Query(
                Criteria.where("_id").is(request.getEventId())
        );
        Map eventDoc = mongoTemplate.findOne(
                eventQuery, Map.class, "events"
        );

        // WHY: Event must exist
        if (eventDoc == null) {
            throw new RuntimeException("Event not found");
        }

        String status = (String) eventDoc.get("eventStatus");
        if (!"ACTIVE".equals(status)) {
            throw new RuntimeException("Event is not active");
        }

        int availableSeats = (int) eventDoc.get("availableSeats");
        if (availableSeats < request.getSeatsBooked()) {
            throw new RuntimeException("Not enough seats available");
        }

//        if (bookingRepository.existsByCustomerIdAndEventId(
//                request.getCustomerId(), request.getEventId())) {
//            throw new RuntimeException(
//                    "Customer already booked this event");
//        }

        Query updateQuery = new Query(
                Criteria.where("_id").is(request.getEventId())
                        .and("availableSeats")
                        .gte(request.getSeatsBooked())
        );

        Update update = new Update().inc(
                "availableSeats", -request.getSeatsBooked()
        );
        var updateResult = mongoTemplate.updateFirst(
                updateQuery, update, "events"
        );

        // WHY: If update failed someone else grabbed the seats
        if (updateResult.getModifiedCount() == 0) {
            throw new RuntimeException(
                    "Booking failed - seats no longer available");
        }

        // WHY: Save booking to MongoDB
        Booking booking = new Booking();
        booking.setEventId(request.getEventId());
        booking.setCustomerId(request.getCustomerId());
        booking.setSeatsBooked(request.getSeatsBooked());
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setBookedAt(LocalDateTime.now().toString());

        Booking savedBooking = bookingRepository.save(booking);

        BookingEvent bookingEvent = new BookingEvent(
                "BOOKING_CONFIRMED",
                savedBooking.getId(),
                savedBooking.getCustomerId(),
                savedBooking.getEventId(),
                savedBooking.getSeatsBooked()
        );
        bookingProducer.sendBookingConfirmed(bookingEvent);

        return mapToResponse(savedBooking);
    }

    // CANCEL BOOKING
    public BookingResponse cancelBooking(String bookingId) {

        // WHY: Find the booking first
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new RuntimeException("Booking not found"));

        // WHY: Can't cancel already cancelled booking
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new RuntimeException("Booking already cancelled");
        }

        // WHY: Release seats back to event
        // so other customers can book them
        Query updateQuery = new Query(
                Criteria.where("_id").is(booking.getEventId())
        );
        Update update = new Update().inc(
                "availableSeats", booking.getSeatsBooked()
        );
        mongoTemplate.updateFirst(updateQuery, update, "events");

        // WHY: Update booking status to CANCELLED
        booking.setStatus(BookingStatus.CANCELLED);
        Booking updatedBooking = bookingRepository.save(booking);

        BookingEvent bookingEvent = new BookingEvent(
                "BOOKING_CANCELLED",
                updatedBooking.getId(),
                updatedBooking.getCustomerId(),
                updatedBooking.getEventId(),
                updatedBooking.getSeatsBooked()
        );
        bookingProducer.sendBookingCancelled(bookingEvent);

        return mapToResponse(updatedBooking);
    }

    // GET BOOKING BY ID
    public BookingResponse getBookingById(String bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new RuntimeException("Booking not found"));
        return mapToResponse(booking);
    }

    // GET BOOKINGS BY CUSTOMER
    public List<BookingResponse> getBookingsByCustomer(
            String customerId) {
        return bookingRepository
                .findByCustomerId(customerId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // GET BOOKINGS BY EVENT
    public List<BookingResponse> getBookingsByEvent(
            String eventId) {
        return bookingRepository
                .findByEventId(eventId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // PRIVATE HELPER
    private BookingResponse mapToResponse(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getEventId(),
                booking.getCustomerId(),
                booking.getSeatsBooked(),
                booking.getStatus(),
                booking.getBookedAt()
        );
    }
}
