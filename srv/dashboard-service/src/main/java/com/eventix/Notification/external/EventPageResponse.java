package com.eventix.Notification.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * DTO that maps to the paginated response from event-service
 * Endpoint: GET /api/events/organizer/{organizerId}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record EventPageResponse(
    List<EventResponse> content,
    int totalPages,
    long totalElements,
    int size,
    int number
) {
}

