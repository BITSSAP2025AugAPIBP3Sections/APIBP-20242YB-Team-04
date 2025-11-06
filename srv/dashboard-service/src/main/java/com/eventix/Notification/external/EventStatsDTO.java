package com.eventix.Notification.external;

/**
 * DTO that maps to the JSON response from event-service
 * Endpoint: GET /api/events/stats?organizerId={organizerId}
 *
 * Expected JSON:
 * {
 *   "totalEvents": 8,
 *   "publishedEvents": 6
 * }
 */
public record EventStatsDTO(
    long totalEvents,
    long publishedEvents
) {
}

