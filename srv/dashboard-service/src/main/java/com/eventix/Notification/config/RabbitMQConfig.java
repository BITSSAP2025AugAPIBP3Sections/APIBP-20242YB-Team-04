package com.eventix.Notification.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ Configuration for the Notification Consumer Service.
 * This configures the queue to listen to and the JSON message converter.
 */
@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "booking.notifications.q";

    /**
     * Declares the queue that this service will listen to.
     * 'true' makes the queue durable (survives broker restarts).
     */
    @Bean
    public Queue bookingNotificationQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    /**
     * Configures Jackson JSON message converter.
     * This automatically deserializes JSON messages into Java objects.
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}

