package com.eventix.search.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI eventixSearchServiceAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Eventix Search Service API")
                        .description("Search microservice for Eventix — handles search, filtering, and recommendations.")
                        .version("1.0.0"));
    }
}