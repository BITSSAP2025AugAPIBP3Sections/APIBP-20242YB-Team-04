package com.eventix.booking.service;

import com.eventix.booking.config.RabbitMQConfig;
import com.eventix.booking.dto.NotificationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service to publish booking notification messages to RabbitMQ.
 * This acts as the producer that sends messages to the notification service consumer.
 */
@Service
public class NotificationPublisher {

    private static final Logger logger = LoggerFactory.getLogger(NotificationPublisher.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * Publishes a booking notification message to the RabbitMQ queue.
     * The notification service will consume this message and send an email.
     *
     * @param userEmail   The email address of the user
     * @param userName    The name of the user
     * @param eventName   The name of the event
     * @param bookingId   The unique booking identifier
     */
    public void publishBookingNotification(String userEmail, String userName, String eventName, String bookingId) {
        try {
            NotificationMessage message = new NotificationMessage(userEmail, userName, eventName, bookingId);

            logger.info("📤 Publishing booking notification to queue: {}", RabbitMQConfig.QUEUE_NAME);
            logger.debug("Message details - BookingID: {}, UserEmail: {}, EventName: {}",
                bookingId, userEmail, eventName);

            rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME, message);

            logger.info("✅ Booking notification published successfully for booking ID: {}", bookingId);

        } catch (Exception e) {
            logger.error("❌ Failed to publish booking notification for booking ID: {}. Error: {}",
                bookingId, e.getMessage(), e);
            // Don't throw exception - we don't want to fail the booking if notification fails
            // The booking should still succeed even if the notification can't be sent
        }
    }
}

