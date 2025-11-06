package com.eventix.Notification.external;

import java.math.BigDecimal;

/**
 * DTO that maps to the JSON response from booking-service
 * Endpoint: GET /api/bookings/stats?organizerId={organizerId}
 *
 * Expected JSON:
 * {
 *   "totalBookings": 150,
 *   "confirmedAttendees": 135,
 *   "totalRevenue": 15000.00
 * }
 */
public record BookingStatsDTO(
    long totalBookings,
    long confirmedAttendees,
    BigDecimal totalRevenue
) {
}

