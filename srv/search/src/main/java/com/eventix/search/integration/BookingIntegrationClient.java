package com.eventix.search.integration;

import org.springframework.stereotype.Component;
import com.eventix.search.dto.EventDTO;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class BookingIntegrationClient {

    public Map<String, Integer> getTrendingEventStats() {
        // Mock popularity data (eventId → bookings count)
        return Map.of(
            "E101", 240,
            "E102", 310,
            "E103", 120
        );
    }

    public Map<String, Double> getCategoryPopularityStats() {
        return Map.of(
            "Music", 78.5,
            "Technology", 92.3,
            "Sports", 60.2
        );
    }

    // ✅ Mock event list for demonstration
    private final List<EventDTO> mockEvents = List.of(
        new EventDTO("E101", "Music Fiesta", "Music", "Delhi", "Central Park",
                LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(2).plusHours(3),
                "ORG123", 85.0),
        new EventDTO("E102", "Tech Expo 2025", "Technology", "Bangalore", "Tech Park",
                LocalDateTime.now().plusDays(5), LocalDateTime.now().plusDays(5).plusHours(8),
                "ORG456", 92.5),
        new EventDTO("E103", "Sports Gala", "Sports", "Mumbai", "National Stadium",
                LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(3).plusHours(4),
                "ORG789", 76.0)
    );

    // ✅ This is the method SearchService expects
    public List<EventDTO> getTrendingEvents(String city, String category, int limit) {
        Map<String, Integer> popularity = getTrendingEventStats();

        return mockEvents.stream()
                .filter(e -> city == null || e.getCity().equalsIgnoreCase(city))
                .filter(e -> category == null || e.getCategory().equalsIgnoreCase(category))
                .sorted((a, b) -> Integer.compare(
                        popularity.getOrDefault(b.getEventId(), 0),
                        popularity.getOrDefault(a.getEventId(), 0)
                ))
                .limit(limit)
                .collect(Collectors.toList());
    }
}
