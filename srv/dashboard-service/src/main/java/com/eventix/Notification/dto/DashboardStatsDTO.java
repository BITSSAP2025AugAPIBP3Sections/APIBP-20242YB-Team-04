package com.eventix.Notification.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

/**
 * Dashboard Stats DTO - Final combined response sent to the frontend
 * This aggregates data from both booking-service and event-service
 */
@Schema(description = "Aggregated dashboard statistics for an event organizer")
public record DashboardStatsDTO(
    @Schema(description = "Total number of events created by the organizer", example = "8")
    long totalEvents,

    @Schema(description = "Number of events that are currently published", example = "6")
    long publishedEvents,

    @Schema(description = "Total number of bookings across all events", example = "150")
    long totalBookings,

    @Schema(description = "Number of confirmed attendees", example = "135")
    long confirmedAttendees,

    @Schema(description = "Total revenue generated from all bookings", example = "45000.00")
    BigDecimal totalRevenue
) {
}

