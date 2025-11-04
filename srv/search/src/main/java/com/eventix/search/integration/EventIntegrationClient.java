package com.eventix.search.integration;

import com.eventix.search.dto.EventDTO;
import com.eventix.search.dto.CalenderEventDTO;
import com.eventix.search.dto.FilterOptionsDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class EventIntegrationClient {

    // Mock data
    public List<EventDTO> getAllEvents() {
        return Arrays.asList(
                new EventDTO("E101", "Music Fiesta", "Music", "Delhi", "Central Park",
                        LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(2).plusHours(3),
                        "ORG123", 85.0),
                new EventDTO("E102", "Tech Expo 2025", "Technology", "Bangalore", "Tech Park",
                        LocalDateTime.now().plusDays(5), LocalDateTime.now().plusDays(5).plusHours(8),
                        "ORG456", 92.5),
                new EventDTO("E103", "Art Carnival", "Art", "Mumbai", "Kala Ghoda",
                        LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(5),
                        "ORG789", 75.0)
        );
    }

    // 🔍 Search
    public List<EventDTO> searchEvents(
            String q, String city, String category,
            String startDate, String endDate,
            String sortBy, int page, int limit
    ) {
        return getAllEvents().stream()
                .filter(e -> q == null || e.getTitle().toLowerCase().contains(q.toLowerCase()))
                .filter(e -> city == null || e.getCity().equalsIgnoreCase(city))
                .filter(e -> category == null || e.getCategory().equalsIgnoreCase(category))
                .limit(limit)
                .collect(Collectors.toList());
    }

    // ⭐ Trending
    public List<EventDTO> getTrendingEvents(String city, String category, int limit) {
        return getAllEvents().stream()
                .filter(e -> city == null || e.getCity().equalsIgnoreCase(city))
                .filter(e -> category == null || e.getCategory().equalsIgnoreCase(category))
                .sorted((a, b) -> Double.compare(b.getPopularityScore(), a.getPopularityScore()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    // 📅 Calendar view
    public CalenderEventDTO getEventsByCalendar(String month, String city, String category) {
        List<EventDTO> filtered = getAllEvents().stream()
                .filter(e -> city == null || e.getCity().equalsIgnoreCase(city))
                .filter(e -> category == null || e.getCategory().equalsIgnoreCase(category))
                .filter(e -> e.getStartDate().getMonth().name().equalsIgnoreCase(month))
                .sorted(Comparator.comparing(EventDTO::getStartDate))
                .collect(Collectors.toList());
        return new CalenderEventDTO(month, filtered);
    }

    // 🧭 Filter options
    public FilterOptionsDTO getFilters() {
        return new FilterOptionsDTO(getAllCategories(), getAllCities());
    }

    // 💡 Suggestions
    public List<String> getSuggestions(String q, String type) {
        if (type != null && type.equalsIgnoreCase("category")) {
            return getAllCategories().stream()
                    .filter(c -> c.toLowerCase().contains(q.toLowerCase()))
                    .collect(Collectors.toList());
        } else {
            return getAllEvents().stream()
                    .map(EventDTO::getTitle)
                    .filter(t -> t.toLowerCase().contains(q.toLowerCase()))
                    .collect(Collectors.toList());
        }
    }

    // 🗺️ Map-based events
    public List<EventDTO> getMapEvents(double lat, double lon, double radius, String category, String date) {
        // Just return filtered mock data (no real geo logic)
        return getAllEvents().stream()
                .filter(e -> category == null || e.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    // 🆕 Recent events
    public List<EventDTO> getRecentEvents(int limit, String city) {
        return getAllEvents().stream()
                .filter(e -> city == null || e.getCity().equalsIgnoreCase(city))
                .sorted(Comparator.comparing(EventDTO::getStartDate))
                .limit(limit)
                .collect(Collectors.toList());
    }

    // 📂 Categories and Cities
    public List<String> getAllCategories() {
        return List.of("Music", "Technology", "Art", "Sports");
    }

    public List<String> getAllCities() {
        return List.of("Delhi", "Mumbai", "Bangalore", "Pune");
    }
}
