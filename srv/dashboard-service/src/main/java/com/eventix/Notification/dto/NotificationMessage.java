package com.eventix.Notification.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;

/**
 * Message DTO that represents the notification payload received from RabbitMQ.
 * This must match the structure sent by the booking-service producer.
 * Using a record for immutability and automatic getters.
 */
@Schema(description = "Notification message for booking confirmations")
public record NotificationMessage(
    @Schema(description = "Email address of the user", example = "john.doe@example.com", required = true)
    String userEmail,

    @Schema(description = "Full name of the user", example = "John Doe", required = true)
    String userName,

    @Schema(description = "Name of the event", example = "Spring Boot Workshop", required = true)
    String eventName,

    @Schema(description = "Unique booking identifier", example = "booking-abc-123", required = true)
    String bookingId
) implements Serializable {
}

