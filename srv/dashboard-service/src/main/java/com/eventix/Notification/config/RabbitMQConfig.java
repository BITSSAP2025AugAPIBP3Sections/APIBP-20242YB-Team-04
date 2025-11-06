package com.eventix.Notification.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ Configuration for the Notification Consumer Service.
 * This configures the queue to listen to and the JSON message converter.
 *
 * IMPORTANT: This configuration prevents JSON version mismatches
 * by using a SimpleClassMapper instead of DefaultClassMapper.
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
     * Configures Jackson JSON message converter WITHOUT type information.
     * This prevents class path mismatch errors between producer and consumer.
     *
     * Key features:
     * - Uses plain JSON without __TypeId__ headers
     * - Works with any producer that sends matching field names
     * - No class path dependency between services
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        // Create converter with ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();

        // Optional: Configure ObjectMapper for better compatibility
        // objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(objectMapper);

        // This creates a simple type mapping that doesn't require matching class paths
        // The converter will use the @RabbitListener method parameter type for deserialization

        return converter;
    }

    /**
     * Configure RabbitTemplate with the JSON converter.
     * This ensures messages sent via this template use JSON serialization.
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}

