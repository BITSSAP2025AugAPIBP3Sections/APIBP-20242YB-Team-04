package com.eventix.Notification.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO that maps to the Event response from event-service
 * Used to extract eventId from the events returned by the organizer endpoint
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record EventResponse(
    String id,
    String title,
    String description,
    String organizerId,
    String location,
    String category,
    String startTime,
    String endTime,
    Integer capacity,
    Double ticketPrice
) {
}

