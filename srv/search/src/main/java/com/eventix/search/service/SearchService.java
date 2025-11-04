package com.eventix.search.service;

import com.eventix.search.dto.CalenderEventDTO;
import com.eventix.search.dto.EventDTO;
import com.eventix.search.dto.FilterOptionsDTO;
import com.eventix.search.dto.SearchResponseDTO;
import com.eventix.search.integration.AuthIntegrationClient;
import com.eventix.search.integration.BookingIntegrationClient;
import com.eventix.search.integration.EventIntegrationClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final EventIntegrationClient eventServiceClient;
    private final BookingIntegrationClient bookingServiceClient;
    private final AuthIntegrationClient authServiceClient;

    public SearchResponseDTO searchEvents(String q, String city, String category,
                                          String startDate, String endDate,
                                          String sortBy, int page, int limit) {
        List<EventDTO> events = eventServiceClient.searchEvents(q, city, category, startDate, endDate, sortBy, page, limit);
        return new SearchResponseDTO(events);
    }

    public List<EventDTO> getTrendingEvents(String city, String category, int limit) {
        return bookingServiceClient.getTrendingEvents(city, category, limit);
    }

    public CalenderEventDTO getEventsByCalendar(String month, String city, String category) {
        return eventServiceClient.getEventsByCalendar(month, city, category);
    }

    public FilterOptionsDTO getAvailableFilters() {
        return eventServiceClient.getFilters();
    }

    public List<String> getSuggestions(String q, String type) {
        return eventServiceClient.getSuggestions(q, type);
    }

    public List<EventDTO> getMapEvents(double lat, double lon, double radius,
                                       String category, String date) {
        return eventServiceClient.getMapEvents(lat, lon, radius, category, date);
    }

    public List<EventDTO> getRecentEvents(int limit, String city) {
        return eventServiceClient.getRecentEvents(limit, city);
    }
}
