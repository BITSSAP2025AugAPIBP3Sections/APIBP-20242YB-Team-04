package com.eventix.search.integration;

import com.eventix.search.dto.EventDTO;
import com.eventix.search.dto.CalenderEventDTO;
import com.eventix.search.dto.FilterOptionsDTO;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class EventIntegrationClient {

    public List<EventDTO> getAllEvents() {
        ZoneId zone = ZoneId.of("Asia/Kolkata");
        return Arrays.asList(
                new EventDTO("E201", "Beach Cleanup Drive", "Environment", "Mumbai", "Juhu Beach",
                        ZonedDateTime.now(zone).plusDays(2), ZonedDateTime.now(zone).plusDays(2).plusHours(4),
                        "ORG901", 88.5),
                new EventDTO("E202", "Tree Plantation Marathon", "Nature", "Bangalore", "Cubbon Park",
                ZonedDateTime.now(zone).plusDays(3), ZonedDateTime.now(zone).plusDays(3).plusHours(5),
                        "ORG902", 91.2),
                new EventDTO("E203", "Blood Donation Camp", "Health", "Delhi", "City Hospital",
                ZonedDateTime.now(zone).plusDays(1), ZonedDateTime.now(zone).plusDays(1).plusHours(6),
                        "ORG903", 85.0),
                new EventDTO("E204", "Food Distribution Drive", "Social Welfare", "Pune", "Community Center",
                ZonedDateTime.now(zone).plusDays(5), ZonedDateTime.now(zone).plusDays(5).plusHours(3),
                        "ORG904", 82.3)
        );
    }
    

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

    public List<EventDTO> getTrendingEvents(String city, String category, int limit) {
        return getAllEvents().stream()
                .filter(e -> city == null || e.getCity().equalsIgnoreCase(city))
                .filter(e -> category == null || e.getCategory().equalsIgnoreCase(category))
                .sorted((a, b) -> Double.compare(b.getPopularityScore(), a.getPopularityScore()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public CalenderEventDTO getEventsByCalendar(String month, String city, String category) {
        List<EventDTO> filtered = getAllEvents().stream()
                .filter(e -> city == null || e.getCity().equalsIgnoreCase(city))
                .filter(e -> category == null || e.getCategory().equalsIgnoreCase(category))
                .filter(e -> e.getStartDate().withZoneSameInstant(ZoneId.of("Asia/Kolkata")).getMonth().name().equalsIgnoreCase(month))
                .sorted(Comparator.comparing(EventDTO::getStartDate))
                .collect(Collectors.toList());
        return new CalenderEventDTO(month, filtered);
    }

    public FilterOptionsDTO getFilters() {
        return new FilterOptionsDTO(getAllCategories(), getAllCities(), getAllTags());
    }

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

    public List<EventDTO> getMapEvents(double lat, double lon, double radius, String category, String date) {
        return getAllEvents().stream()
                .filter(e -> category == null || e.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    public List<EventDTO> getRecentEvents(int limit, String city) {
        return getAllEvents().stream()
                .filter(e -> city == null || e.getCity().equalsIgnoreCase(city))
                .sorted(Comparator.comparing(EventDTO::getStartDate))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<String> getAllCategories() {
        return List.of("Environmental", "Education", "Health", "Animal Welfare", "Community Service");
    }

    public List<String> getAllCities() {
        return List.of("Mumbai", "Delhi", "Bangalore", "Pune", "Hyderabad", "Chennai");
    }

    public List<String> getAllTags() {
        return List.of("Cleanup", "Awareness", "Teaching", "Fundraising", "Medical Aid", "Shelter");
    }
}
