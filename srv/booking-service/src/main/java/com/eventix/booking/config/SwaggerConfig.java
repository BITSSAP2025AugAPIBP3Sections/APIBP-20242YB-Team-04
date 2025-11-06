package com.eventix.booking.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI bookingOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Eventix Booking Service API")
                        .description("API documentation for Eventix Booking Service")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Vishal Kumar Singh")
                                .email("vkrs362@example.com")
                                .url("https://github.com/Vkrs362"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org"))
                )
                .externalDocs(new ExternalDocumentation()
                        .description("Project Repository")
                        .url("https://github.com/Vkrs362/Eventix"));
    }
}
