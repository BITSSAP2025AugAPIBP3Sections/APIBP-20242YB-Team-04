package com.eventix.search.integration;

import org.springframework.stereotype.Component;
import com.eventix.search.dto.EventDTO;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class BookingIntegrationClient {

    public Map<String, Integer> getTrendingEventStats() {
        return Map.of(
            "EVT101", 340,
            "EVT102", 295,
            "EVT103", 180,
            "EVT104", 410,
            "EVT105", 260
        );
    }

    public Map<String, Double> getCategoryPopularityStats() {
        return Map.of(
            "Environmental", 91.2,
            "Education", 84.7,
            "Health", 78.9,
            "Animal Welfare", 72.3,
            "Community Service", 88.1
        );
    }

    ZoneId zone = ZoneId.of("Asia/Kolkata");

    private final List<EventDTO> mockEvents = List.of(
        new EventDTO("EVT101", "Beach Cleanup Drive", "Environmental", "Mumbai", "Juhu Beach",
        ZonedDateTime.now(zone).plusDays(2), ZonedDateTime.now(zone).plusDays(2).plusHours(3),
                "ORG001", 95.0),

        new EventDTO("EVT102", "Tree Plantation Marathon", "Environmental", "Pune", "Aundh Park",
        ZonedDateTime.now(zone).plusDays(3), ZonedDateTime.now(zone).plusDays(3).plusHours(4),
                "ORG002", 89.0),

        new EventDTO("EVT103", "Community Health Camp", "Health", "Delhi", "Nehru Nagar Center",
        ZonedDateTime.now(zone).plusDays(5), ZonedDateTime.now(zone).plusDays(5).plusHours(6),
                "ORG003", 92.0),

        new EventDTO("EVT104", "Animal Shelter Volunteering", "Animal Welfare", "Bangalore", "JP Nagar Shelter",
        ZonedDateTime.now(zone).plusDays(7), ZonedDateTime.now(zone).plusDays(7).plusHours(5),
                "ORG004", 87.5),
                
        new EventDTO("EVT105", "Weekend Teaching Program", "Education", "Hyderabad", "Secunderabad Community School",
                ZonedDateTime.now(zone).plusDays(10), ZonedDateTime.now(zone).plusDays(10).plusHours(4),
                "ORG005", 90.0)
    );

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
