package com.eventix.search.integration;

import com.eventix.search.dto.EventDTO;
import com.eventix.search.exception.ServiceUnavailableException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * RestTemplate-based Booking integration client with proper exception propagation.
 *
 * - ResourceAccessException -> ServiceUnavailableException (maps to 503)
 * - HttpClientErrorException / HttpServerErrorException -> rethrown (4xx/5xx forwarded)
 * - Other exceptions -> RuntimeException (becomes 500)
 */
@Component
public class BookingIntegrationClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final ObjectMapper mapper = new ObjectMapper();

    public BookingIntegrationClient(RestTemplate restTemplate,
                                    @Value("${remote.booking.base-url:http://localhost:8082}") String baseUrl) {
        this.restTemplate = restTemplate;
        // normalize baseUrl (no trailing slash)
        this.baseUrl = (baseUrl != null && baseUrl.endsWith("/")) ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    }

    // --------------------------------------------------------------------
    // 1) Trending Event Stats
    // --------------------------------------------------------------------
    public Map<String, Integer> getTrendingEventStats() {
        try {
            ResponseEntity<Map<String, Integer>> resp =
                    restTemplate.exchange(
                            baseUrl + "/bookings/stats/trending",
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<Map<String, Integer>>() {}
                    );

            return resp.getBody() != null ? resp.getBody() : Map.of();

        } catch (ResourceAccessException ex) {
            // network/connect/timeout -> downstream unavailable
            throw new ServiceUnavailableException("Booking service is unavailable.");
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            // forward remote 4xx/5xx to caller
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    // --------------------------------------------------------------------
    // 2) Trending Events (FULL EVENT DTO LIST)
    // --------------------------------------------------------------------
    public List<EventDTO> getTrendingEvents(String city, String category, int limit) {

        UriComponentsBuilder uri = UriComponentsBuilder
                .fromUriString(baseUrl + "/bookings/trending")
                .queryParam("limit", limit);

        if (city != null) uri.queryParam("city", city);
        if (category != null) uri.queryParam("category", category);

        try {
            ResponseEntity<List> resp = restTemplate.getForEntity(uri.toUriString(), List.class);

            if (resp.getBody() == null) return List.of();

            return resp.getBody().stream()
                    .map(o -> mapper.convertValue(o, EventDTO.class))
                    .toList();

        } catch (ResourceAccessException ex) {
            throw new ServiceUnavailableException("Booking service is unavailable.");
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
