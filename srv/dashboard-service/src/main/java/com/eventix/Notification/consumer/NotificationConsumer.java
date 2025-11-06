package com.eventix.Notification.consumer;

import com.eventix.Notification.dto.NotificationMessage;
import com.eventix.Notification.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * RabbitMQ Consumer that listens to the booking notifications queue.
 * When a message arrives, it automatically deserializes the JSON and processes it.
 */
@Component
public class NotificationConsumer {

    private static final Logger logger = LoggerFactory.getLogger(NotificationConsumer.class);

    @Autowired
    private EmailService emailService;

    /**
     * Listens to the "booking.notifications.q" queue and processes messages.
     * The @RabbitListener annotation automatically:
     * 1. Connects to RabbitMQ
     * 2. Subscribes to the specified queue
     * 3. Deserializes JSON messages into NotificationMessage objects
     * 4. Calls this method for each message received
     *
     * @param message The deserialized notification message from the queue
     */
    @RabbitListener(queues = "booking.notifications.q")
    public void handleMessage(NotificationMessage message) {
        logger.info("=== Message Received from Queue ===");
        logger.info("Booking ID: {}", message.bookingId());
        logger.info("User Email: {}", message.userEmail());
        logger.info("User Name: {}", message.userName());
        logger.info("Event Name: {}", message.eventName());

        try {
            // Send the booking confirmation email
            emailService.sendBookingConfirmation(
                message.userEmail(),
                message.userName(),
                message.eventName(),
                message.bookingId()
            );

            logger.info("✅ Notification processed successfully for booking ID: {}", message.bookingId());

        } catch (Exception e) {
            logger.error("❌ Failed to process notification for booking ID: {}. Error: {}",
                message.bookingId(), e.getMessage(), e);

            // By throwing an exception, we signal RabbitMQ that the message processing failed.
            // RabbitMQ will requeue the message and retry it later (based on your configuration).
            // This ensures no notifications are lost due to temporary failures (e.g., SendGrid API down).
            throw new RuntimeException("Email sending failed, message will be requeued.", e);
        }
    }
}

