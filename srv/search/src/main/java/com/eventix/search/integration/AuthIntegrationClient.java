package com.eventix.search.integration;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class AuthIntegrationClient {

    public Map<String, List<String>> getUserInterests(String userId) {
        // Mock user interest data
        return Map.of(
            "U123", List.of("Music", "Technology"),
            "U456", List.of("Sports", "Art")
        );
    }
}
