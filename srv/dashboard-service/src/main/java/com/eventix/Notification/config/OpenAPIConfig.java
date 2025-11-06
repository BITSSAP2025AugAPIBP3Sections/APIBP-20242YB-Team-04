package com.eventix.Notification.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI/Swagger Configuration
 * Provides interactive API documentation at /swagger-ui.html
 */
@Configuration
public class OpenAPIConfig {

    @Value("${server.port:8082}")
    private String serverPort;

    @Bean
    public OpenAPI organizerUtilityServiceAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:" + serverPort);
        devServer.setDescription("Development server");

        Contact contact = new Contact();
        contact.setEmail("support@eventix.com");
        contact.setName("Eventix Team");

        Info info = new Info()
            .title("Organizer Utility Service API")
            .version("1.0.0")
            .contact(contact)
            .description("""
                Dual-purpose microservice providing:
                1. Dashboard API - Aggregates statistics from booking-service and event-service
                2. Notification Consumer - Consumes RabbitMQ messages for email notifications
                
                Access the interactive API documentation at /swagger-ui.html
                """);

        return new OpenAPI()
            .info(info)
            .servers(List.of(devServer));
    }
}

