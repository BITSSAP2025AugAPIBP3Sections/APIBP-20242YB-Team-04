package com.eventix.user_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.client.RestTemplate;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI userServiceOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Eventix Auth User Service API")
                .description("Authentication and User Management Service for Eventix Platform")
                .version("1.0.0")
                .contact(new Contact()
                    .name("Eventix Development Team")
                    .url("https://eventix.com")
                    .email("dev@eventrix.com"))
                .license(new License()
                    .name("MIT License")
                    .url("https://opensource.org/licenses/MIT"))
            );
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
