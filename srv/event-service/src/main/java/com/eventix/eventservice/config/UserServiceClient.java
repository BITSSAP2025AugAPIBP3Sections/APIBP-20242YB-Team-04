package com.eventix.eventservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class UserServiceClient {

    @Value("${user.service.url:http://localhost:8081}")
    private String userServiceUrl;

    private final RestTemplate restTemplate;

    public UserServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Validate if a user exists by userId
     */
    public boolean validateUser(String userId) {
        try {
            String url = userServiceUrl + "/users/validate/" + userId;
            ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);
            return Boolean.TRUE.equals(response.getBody());
        } catch (Exception e) {
            // Log the error and return false if user service is down or user doesn't exist
            System.err.println("Error validating user " + userId + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Get user details (id, name, email) by userId
     */
    @SuppressWarnings("unchecked")
    public Map<String, String> getUserDetails(String userId) {
        try {
            String url = userServiceUrl + "/users/details/" + userId;
            return restTemplate.getForObject(url, Map.class);
        } catch (Exception e) {
            System.err.println("Error fetching user details for " + userId + ": " + e.getMessage());
            return null;
        }
    }
}
