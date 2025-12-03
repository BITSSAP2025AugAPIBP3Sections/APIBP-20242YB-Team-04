package com.eventix.eventservice.config;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceClient {

    private static final String USER_SERVICE_CB = "userServiceCB";

    @Value("${user.service.url:http://localhost:8081}")
    private String userServiceUrl;

    private final RestTemplate restTemplate;

    public UserServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Validate if a user exists by userId
     */
    @CircuitBreaker(name = USER_SERVICE_CB, fallbackMethod = "validateUserFallback")
    public boolean validateUser(String userId) {
        String url = userServiceUrl + "/users/validate/" + userId;
        ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);
        return Boolean.TRUE.equals(response.getBody());
    }

    /**
     * Fallback for validateUser
     * Must match signature + Throwable
     */
    public boolean validateUserFallback(String userId, Throwable t) {
        System.err.println("CB fallback: validateUser failed for " + userId + " -> " + t.getMessage());
        return false; // safest failure mode
    }

    /**
     * Get user details by userId
     */
    @CircuitBreaker(name = USER_SERVICE_CB, fallbackMethod = "getUserDetailsFallback")
    public Map<String, String> getUserDetails(String userId) {
        String url = userServiceUrl + "/users/details/" + userId;
        return restTemplate.getForObject(url, Map.class);
    }

    /**
     * Fallback for getUserDetails
     */
    public Map<String, String> getUserDetailsFallback(String userId, Throwable t) {
        System.err.println("CB fallback: getUserDetails failed for " + userId + " -> " + t.getMessage());
        return new HashMap<>(); // return empty map instead of null
    }
}
