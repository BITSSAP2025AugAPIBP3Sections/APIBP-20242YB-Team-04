package com.eventix.search.service;

import com.eventix.search.dto.CalenderEventDTO;
import com.eventix.search.dto.EventDTO;
import com.eventix.search.dto.FilterOptionsDTO;
import com.eventix.search.dto.SearchResponseDTO;
import com.eventix.search.integration.AuthIntegrationClient;
import com.eventix.search.integration.BookingIntegrationClient;
import com.eventix.search.integration.EventIntegrationClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private static final Logger accessLog = LoggerFactory.getLogger("access");
    private static final Logger debugLog = LoggerFactory.getLogger("debug");
    private static final Logger errorLog = LoggerFactory.getLogger("error");

    private final EventIntegrationClient eventServiceClient;
    private final BookingIntegrationClient bookingServiceClient;
    private final AuthIntegrationClient authServiceClient;

    public SearchResponseDTO searchEvents(String q, String city, String category,
                                          String startDate, String endDate,
                                          String sortBy, int page, int limit) {

        accessLog.info("searchEvents called city={}, category={}, q={}", city, category, q);

        try {
            debugLog.debug("Forwarding search request to event service");
            List<EventDTO> events = eventServiceClient.searchEvents(
                    q, city, category, startDate, endDate, sortBy, page, limit);

            return new SearchResponseDTO(events);

        } catch (Exception e) {
            errorLog.error("searchEvents failed: {}", e);
            throw e;
        }
    }

    public List<EventDTO> getTrendingEvents(String city, String category, int limit) {

        accessLog.info("getTrendingEvents called city={}, category={}", city, category);

        try {
            return bookingServiceClient.getTrendingEvents(city, category, limit);
        } catch (Exception e) {
            errorLog.error("getTrendingEvents failed: {}", e);
            throw e;
        }
    }

    public CalenderEventDTO getEventsByCalendar(String month, String city, String category) {

        accessLog.info("getEventsByCalendar month={}, city={}", month, city);

        try {
            return eventServiceClient.getEventsByCalendar(month, city, category);
        } catch (Exception e) {
            errorLog.error("getEventsByCalendar failed: {}", e);
            throw e;
        }
    }

    public FilterOptionsDTO getAvailableFilters() {

        accessLog.info("getAvailableFilters called");

        try {
            return eventServiceClient.getFilters();
        } catch (Exception e) {
            errorLog.error("getAvailableFilters failed: {}", e);
            throw e;
        }
    }

    public List<String> getSuggestions(String q, String type) {

        accessLog.info("getSuggestions q={}, type={}", q, type);

        try {
            return eventServiceClient.getSuggestions(q, type);
        } catch (Exception e) {
            errorLog.error("getSuggestions failed: {}", e);
            throw e;
        }
    }

    public List<EventDTO> getMapEvents(double lat, double lon, double radius,
                                       String category, String date) {

        accessLog.info("getMapEvents lat={}, lon={}, radius={}", lat, lon, radius);

        try {
            return eventServiceClient.getMapEvents(lat, lon, radius, category, date);
        } catch (Exception e) {
            errorLog.error("getMapEvents failed: {}", e);
            throw e;
        }
    }

    public List<EventDTO> getRecentEvents(int limit, String city) {

        accessLog.info("getRecentEvents limit={}, city={}", limit, city);

        try {
            return eventServiceClient.getRecentEvents(limit, city);
        } catch (Exception e) {
            errorLog.error("getRecentEvents failed: {}", e);
            throw e;
        }
    }

    public List<EventDTO> getPersonalizedEvents(String userId, int limit, String basedOn) {

        accessLog.info("getPersonalizedEvents userId={}, basedOn={}", userId, basedOn);

        try {
            return authServiceClient.getUserInterests(userId, limit, basedOn);
        } catch (Exception e) {
            errorLog.error("getPersonalizedEvents failed: {}", e);
            throw e;
        }
    }
}
