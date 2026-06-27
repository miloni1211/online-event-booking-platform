package com.eventbooking.paymentservice.repository;

import com.eventbooking.paymentservice.model.Payment;
import com.eventbooking.paymentservice.model.PaymentStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository
        extends MongoRepository<Payment, String> {

    // WHY: Find payment by booking ID
    // to check payment status for a booking
    Optional<Payment> findByBookingId(String bookingId);

    // WHY: Get all payments by customer
    // for payment history
    List<Payment> findByCustomerId(String customerId);

    // WHY: Get payments by status
    // e.g. find all pending payments
    List<Payment> findByStatus(PaymentStatus status);
}