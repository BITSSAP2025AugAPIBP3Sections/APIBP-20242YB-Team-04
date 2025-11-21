package com.eventix.booking.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ Configuration for the Booking Service (Producer).
 * This configures the queue and the JSON message converter for publishing messages.
 */
@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "booking.notifications.q";

    /**
     * Declares the queue that this service will publish to.
     * 'true' makes the queue durable (survives broker restarts).
     * If the queue already exists, this declaration is idempotent.
     */
    @Bean
    public Queue bookingNotificationQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    /**
     * Configures Jackson JSON message converter WITHOUT type information.
     * This ensures compatibility with the notification service consumer.
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(objectMapper);
        return converter;
    }

    /**
     * Configure RabbitTemplate with the JSON converter.
     * This template is used to publish messages to RabbitMQ.
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}

