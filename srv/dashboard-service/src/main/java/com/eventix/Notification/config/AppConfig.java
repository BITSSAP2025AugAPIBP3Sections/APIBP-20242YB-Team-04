package com.eventix.Notification.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Application Configuration
 * Provides RestTemplate bean for making HTTP calls to other microservices
 */
@Configuration
public class AppConfig {

    /**
     * RestTemplate bean used by DashboardService to call booking-service and event-service
     * This allows us to make synchronous HTTP requests to aggregate data
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

