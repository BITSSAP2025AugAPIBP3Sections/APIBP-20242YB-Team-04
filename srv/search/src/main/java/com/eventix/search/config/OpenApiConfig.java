package com.eventix.search.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

import java.util.List;

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
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Eventix Dev Team")
                                .email("support@eventix.io")
                                .url("https://eventix.io"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                        .servers(List.of(
                                new Server().url("http://localhost:8083").description("Search Service Local Search")
                        ));
    }
}