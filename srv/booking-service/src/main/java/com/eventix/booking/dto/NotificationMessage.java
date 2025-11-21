package com.eventix.booking.dto;

import java.io.Serializable;

/**
 * Notification message DTO for publishing booking events to RabbitMQ.
 * This matches the structure expected by the notification service consumer.
 */
public record NotificationMessage(
    String userEmail,
    String userName,
    String eventName,
    String bookingId
) implements Serializable {
}

