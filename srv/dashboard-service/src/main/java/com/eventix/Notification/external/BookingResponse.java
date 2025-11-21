package com.eventix.Notification.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO that maps to the Booking response from booking-service
 * Endpoint: GET /api/bookings/by-events?eventIds=...
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record BookingResponse(
    String id,
    String eventId,
    String userId,
    Integer totalBookings,
    Integer recentBookings,
    Integer wishlistCount,
    Double avgRating,
    String timestamp,
    String city,
    String category,
    String status,
    Boolean wishlisted
) {
}

