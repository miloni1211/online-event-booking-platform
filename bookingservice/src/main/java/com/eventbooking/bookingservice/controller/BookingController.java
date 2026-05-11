package com.eventbooking.bookingservice.controller;

import com.eventbooking.bookingservice.dto.BookingRequest;
import com.eventbooking.bookingservice.dto.BookingResponse;
import com.eventbooking.bookingservice.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingResponse> bookTicket(
            @RequestBody BookingRequest request) {
        BookingResponse response = bookingService
                .bookTicket(request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<BookingResponse> cancelBooking(
            @PathVariable String id) {
        BookingResponse response = bookingService
                .cancelBooking(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBookingById(
            @PathVariable String id) {
        BookingResponse response = bookingService
                .getBookingById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<BookingResponse>> getBookingsByCustomer(
            @PathVariable String customerId) {
        List<BookingResponse> responses = bookingService
                .getBookingsByCustomer(customerId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<BookingResponse>> getBookingsByEvent(
            @PathVariable String eventId) {
        List<BookingResponse> responses = bookingService
                .getBookingsByEvent(eventId);
        return ResponseEntity.ok(responses);
    }
}
