package com.eventix.search.integration;

import com.eventix.search.dto.EventDTO;
import com.eventix.search.exception.ServiceUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class AuthIntegrationClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public AuthIntegrationClient(RestTemplate restTemplate,
                                 @Value("${remote.auth.base-url:http://localhost:8081}") String baseUrl) {
        this.restTemplate = restTemplate;
        // normalize baseUrl (no trailing slash)
        this.baseUrl = baseUrl != null && baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    }

    /**
     * Calls: GET {baseUrl}/users/{userId}/interests?limit={limit}&basedOn={basedOn}
     * Returns list of EventDTO (or empty list). Errors are propagated as:
     *  - ResourceAccessException -> ServiceUnavailableException (503)
     *  - HttpClientErrorException -> rethrown (4xx forwarded)
     *  - other exceptions -> RuntimeException (500)
     */
    public List<EventDTO> getUserInterests(String userId, int limit, String basedOn) {
        String uri = UriComponentsBuilder
                .fromUriString(baseUrl + "/users/{userId}/interests")
                .queryParam("limit", limit)
                .queryParamIfPresent("basedOn", java.util.Optional.ofNullable(basedOn))
                .buildAndExpand(userId)
                .toUriString();

        try {
            ResponseEntity<EventDTO[]> resp = restTemplate.getForEntity(uri, EventDTO[].class);
            EventDTO[] arr = resp.getBody();
            return arr == null ? List.of() : Arrays.asList(arr);
        } catch (ResourceAccessException ex) {
            // network/connect/timeout -> treat as downstream unavailable
            throw new ServiceUnavailableException("Auth service is unavailable.");
        } catch (HttpClientErrorException ex) {
            // forward 4xx (and 5xx if thrown as HttpClientErrorException)
            throw ex;
        } catch (Exception ex) {
            // unexpected -> let global handler map to 500
            throw new RuntimeException(ex);
        }
    }
}
