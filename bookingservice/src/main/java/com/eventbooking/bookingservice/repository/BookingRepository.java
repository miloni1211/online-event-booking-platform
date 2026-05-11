package com.eventbooking.bookingservice.repository;

import com.eventbooking.bookingservice.model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends MongoRepository<Booking,String> {
    List<Booking> findByCustomerId(String customerId);
    List<Booking> findByEventId(String eventId);

    boolean existsByCustomerIdAndEventId(
            String customerId,
            String eventId);

}
