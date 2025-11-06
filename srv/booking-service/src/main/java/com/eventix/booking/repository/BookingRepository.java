package com.eventix.booking.repository;

import com.eventix.booking.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {
    List<Booking> findByUserId(String userId);
    List<Booking> findByCity(String city);
    List<Booking> findByCategory(String category);
    List<Booking> findByEventId(String eventId);
}

