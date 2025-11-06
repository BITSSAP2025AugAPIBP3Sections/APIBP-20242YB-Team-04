package com.eventix.booking.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.Instant;
import java.util.Map;

/**
 * Represents a booking in the Eventix system.
 * Also contains a static nested DTO (StatsResponse) for GraphQL aggregations.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String eventId;
    private String userId;

    // Aggregate or analytical fields
    private Integer totalBookings;
    private Integer recentBookings;
    private Integer wishlistCount;
    private Double avgRating;

    private Instant timestamp = Instant.now();
    private String city;
    private String category;

    private String status = "NEW";
    private Boolean wishlisted = false;

    // ------------------------------------------
    // ✅ Nested class for GraphQL stats response
    // ------------------------------------------
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatsResponse {
        private int totalRecords;
        private long confirmed;
        private double avgRating;
        private Map<String, Long> byCity;
    }
}
