package com.eventix.Notification.controller;

import com.eventix.Notification.dto.NotificationMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * Optional test controller to manually trigger a message to the queue.
 * This is useful for testing without needing the booking service.
 */
@RestController
@RequestMapping("/api/test")
@Tag(name = "Test", description = "Testing and debugging endpoints (development only)")
public class TestController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Operation(
        summary = "Send test notification email",
        description = "Manually trigger a test notification email by publishing a message to RabbitMQ queue"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Test message sent successfully",
            content = @Content(mediaType = "text/plain")
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Failed to send message",
            content = @Content(mediaType = "text/plain")
        )
    })
    @PostMapping("/send-notification")
    public String sendTestNotification(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Notification message details",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = NotificationMessage.class),
                examples = @ExampleObject(
                    name = "Test notification",
                    value = "{\n  \"userEmail\": \"test@example.com\",\n  \"userName\": \"Test User\",\n  \"eventName\": \"Spring Boot Workshop\",\n  \"bookingId\": \"test-001\"\n}"
                )
            )
        )
        @RequestBody NotificationMessage message
    ) {
        try {
            rabbitTemplate.convertAndSend("booking.notifications.q", message);
            return "✅ Test message sent to queue: " + message;
        } catch (Exception e) {
            return "❌ Failed to send message: " + e.getMessage();
        }
    }

    @Operation(
        summary = "Service health check",
        description = "Returns the health status of the notification service"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Service is running"
    )
    @GetMapping("/health")
    public String health() {
        return "Notification Service is running!";
    }
}

