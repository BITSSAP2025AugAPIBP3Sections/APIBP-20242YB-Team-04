package com.eventix.search.integration;

import com.eventix.search.dto.CalenderEventDTO;
import com.eventix.search.dto.EventDTO;
import com.eventix.search.dto.FilterOptionsDTO;
import com.eventix.search.exception.ServiceUnavailableException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class EventIntegrationClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final ObjectMapper mapper = new ObjectMapper();

    public EventIntegrationClient(RestTemplate restTemplate,
                                  @Value("${remote.event.base-url:http://localhost:8083}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    // ------------------------------
    // 1) SEARCH EVENTS (Page)
    // ------------------------------
    public List<EventDTO> searchEvents(String q,
                                       String city,
                                       String category,
                                       String startDate,
                                       String endDate,
                                       String sortBy,
                                       int page,
                                       int limit) {

        UriComponentsBuilder uri = UriComponentsBuilder
                .fromUriString(baseUrl + "/api/v1/events/search")
                .queryParam("page", page)
                .queryParam("size", limit);

        if (city != null) uri.queryParam("city", city);
        if (category != null) uri.queryParam("category", category);
        if (startDate != null) uri.queryParam("from", startDate);
        if (endDate != null) uri.queryParam("to", endDate);

        try {
            ResponseEntity<PageResponse> resp =
                    restTemplate.getForEntity(uri.toUriString(), PageResponse.class);

            if (resp.getBody() != null && resp.getBody().content != null) {
                return resp.getBody().content.stream()
                        .map(e -> mapper.convertValue(e, EventDTO.class))
                        .toList();
            }

        } catch (ResourceAccessException ex) {
            // network/connect/timeout to event-service
            throw new ServiceUnavailableException("Event service is unavailable.");
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            // forward remote 4xx/5xx to client
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return Collections.emptyList();
    }

    // ------------------------------
    // 2) MAP EVENTS
    // ------------------------------
    public List<EventDTO> getMapEvents(double lat, double lon, double radius,
                                       String category, String date) {

        UriComponentsBuilder uri = UriComponentsBuilder
                .fromUriString(baseUrl + "/api/v1/events/map")
                .queryParam("lat", lat)
                .queryParam("lon", lon)
                .queryParam("radius", radius);

        if (category != null) uri.queryParam("category", category);
        if (date != null) uri.queryParam("date", date);

        try {
            ResponseEntity<List> resp =
                    restTemplate.getForEntity(uri.toUriString(), List.class);

            return resp.getBody() == null ? List.of()
                    : resp.getBody().stream()
                        .map(o -> mapper.convertValue(o, EventDTO.class))
                        .toList();

        } catch (ResourceAccessException ex) {
            throw new ServiceUnavailableException("Event service is unavailable.");
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    // ------------------------------
    // 3) RECENT EVENTS
    // ------------------------------
    public List<EventDTO> getRecentEvents(int limit, String city) {

        UriComponentsBuilder uri = UriComponentsBuilder
                .fromUriString(baseUrl + "/api/v1/events/recent")
                .queryParam("limit", limit);

        if (city != null) uri.queryParam("city", city);

        try {
            ResponseEntity<List> resp =
                    restTemplate.getForEntity(uri.toUriString(), List.class);

            return resp.getBody() == null ? List.of()
                    : resp.getBody().stream()
                        .map(o -> mapper.convertValue(o, EventDTO.class))
                        .toList();

        } catch (ResourceAccessException ex) {
            throw new ServiceUnavailableException("Event service is unavailable.");
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    // ------------------------------
    // 4) CALENDAR EVENTS
    // ------------------------------
    public CalenderEventDTO getEventsByCalendar(String month, String city, String category) {

        UriComponentsBuilder uri = UriComponentsBuilder
                .fromUriString(baseUrl + "/api/v1/events/calendar")
                .queryParam("month", month);

        if (city != null) uri.queryParam("city", city);
        if (category != null) uri.queryParam("category", category);

        try {
            ResponseEntity<Map> resp =
                    restTemplate.getForEntity(uri.toUriString(), Map.class);

            if (resp.getBody() == null) return new CalenderEventDTO(month, List.of());

            List<EventDTO> events =
                    ((List<?>) resp.getBody().get("events")).stream()
                            .map(o -> mapper.convertValue(o, EventDTO.class))
                            .toList();

            return new CalenderEventDTO(
                    resp.getBody().get("month").toString(),
                    events
            );

        } catch (ResourceAccessException ex) {
            throw new ServiceUnavailableException("Event service is unavailable.");
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    // ------------------------------
    // 5) FILTER OPTIONS
    // ------------------------------
    public FilterOptionsDTO getFilters() {
        try {
            ResponseEntity<Map> resp =
                    restTemplate.getForEntity(baseUrl + "/api/v1/events/filters", Map.class);

            if (resp.getBody() == null) return new FilterOptionsDTO(List.of(), List.of(), List.of());

            return new FilterOptionsDTO(
                    (List<String>) resp.getBody().getOrDefault("categories", List.of()),
                    (List<String>) resp.getBody().getOrDefault("cities", List.of()),
                    (List<String>) resp.getBody().getOrDefault("tags", List.of())
            );

        } catch (ResourceAccessException ex) {
            throw new ServiceUnavailableException("Event service is unavailable.");
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    // ------------------------------
    // 6) SUGGESTIONS
    // ------------------------------
    public List<String> getSuggestions(String q, String type) {

        UriComponentsBuilder uri = UriComponentsBuilder
                .fromUriString(baseUrl + "/api/v1/events/suggestions")
                .queryParam("q", q);

        if (type != null) uri.queryParam("type", type);

        try {
            ResponseEntity<List> resp =
                    restTemplate.getForEntity(uri.toUriString(), List.class);

            return resp.getBody() == null ? List.of()
                    : resp.getBody().stream()
                        .map(Object::toString)
                        .toList();

        } catch (ResourceAccessException ex) {
            throw new ServiceUnavailableException("Event service is unavailable.");
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    // Page wrapper class
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class PageResponse {
        public List<Object> content;
    }
}
