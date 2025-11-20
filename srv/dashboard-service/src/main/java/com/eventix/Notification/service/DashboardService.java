package com.eventix.Notification.service;

import com.eventix.Notification.dto.DashboardStatsDTO;
import com.eventix.Notification.external.BookingResponse;
import com.eventix.Notification.external.EventPageResponse;
import com.eventix.Notification.external.EventResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Dashboard Service - ROLE 2: Data Aggregation
 *
 * This service "pulls" data from other microservices:
 * 1. Calls event-service to get all events by organizerId
 * 2. Extracts eventIds from the events
 * 3. Calls booking-service to get all bookings for those eventIds
 * 4. Combines the responses into a single DashboardStatsDTO
 */
@Service
public class DashboardService {

    private static final Logger logger = LoggerFactory.getLogger(DashboardService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${service.booking.url}")
    private String bookingServiceUrl;

    @Value("${service.event.url}")
    private String eventServiceUrl;

    /**
     * Gets aggregated dashboard statistics for an organizer
     * Makes HTTP calls to event-service and booking-service
     *
     * @param organizerId The organizer's unique identifier
     * @return Combined dashboard statistics
     * @throws RuntimeException if either service call fails
     */
    public DashboardStatsDTO getOrganizerStats(String organizerId) {

        logger.info("Fetching dashboard stats for organizer: {}", organizerId);

        try {
            // 1. Fetch all events for the organizer from event-service
            List<EventResponse> allEvents = fetchAllEventsByOrganizer(organizerId);

            if (allEvents.isEmpty()) {
                logger.warn("No events found for organizer: {}", organizerId);
                return new DashboardStatsDTO(0, 0, 0, 0, BigDecimal.ZERO);
            }

            logger.info("Found {} events for organizer: {}", allEvents.size(), organizerId);

            // 2. Extract all eventIds from the events
            List<String> eventIds = allEvents.stream()
                    .map(EventResponse::id)
                    .collect(Collectors.toList());

            logger.debug("Extracted {} eventIds: {}", eventIds.size(), eventIds);

            // 3. Fetch all bookings for these eventIds from booking-service
            List<BookingResponse> bookings = fetchBookingsByEventIds(eventIds);

            logger.info("Found {} bookings for organizer's events", bookings.size());

            // 4. Calculate statistics
            long totalEvents = allEvents.size();
            long publishedEvents = totalEvents; // Assuming all fetched events are published
            long totalBookings = bookings.size();
            long confirmedAttendees = bookings.stream()
                    .filter(booking -> "CONFIRMED".equalsIgnoreCase(booking.status()))
                    .count();

            // Calculate total revenue (assuming we might add price field later)
            // For now, returning zero as the Booking model doesn't have a price field
            BigDecimal totalRevenue = BigDecimal.ZERO;

            // 5. Combine all statistics into one DTO
            DashboardStatsDTO combinedStats = new DashboardStatsDTO(
                    totalEvents,
                    publishedEvents,
                    totalBookings,
                    confirmedAttendees,
                    totalRevenue
            );

            logger.info("Dashboard stats aggregation complete for organizer: {} - Events: {}, Bookings: {}, Confirmed: {}",
                    organizerId, totalEvents, totalBookings, confirmedAttendees);
            return combinedStats;

        } catch (Exception e) {
            logger.error("Failed to fetch dashboard stats for organizer {}: {}", organizerId, e.getMessage(), e);
            throw new RuntimeException("Failed to aggregate dashboard statistics: " + e.getMessage(), e);
        }
    }

    /**
     * Fetches all events for an organizer from event-service
     * Handles pagination to retrieve all events
     *
     * @param organizerId The organizer's unique identifier
     * @return List of all events
     */
    private List<EventResponse> fetchAllEventsByOrganizer(String organizerId) {
        List<EventResponse> allEvents = new ArrayList<>();
        int page = 0;
        int size = 100; // Fetch 100 events per page
        boolean hasMorePages = true;

        while (hasMorePages) {
            String eventUrl = UriComponentsBuilder.fromHttpUrl(eventServiceUrl)
                    .path("/api/events/organizer/{organizerId}")
                    .queryParam("page", page)
                    .queryParam("size", size)
                    .buildAndExpand(organizerId)
                    .toUriString();

            logger.debug("Calling event-service (page {}): {}", page, eventUrl);

            EventPageResponse pageResponse = restTemplate.getForObject(eventUrl, EventPageResponse.class);

            if (pageResponse == null || pageResponse.content() == null) {
                logger.error("event-service returned null response for organizer: {}", organizerId);
                break;
            }

            allEvents.addAll(pageResponse.content());
            logger.debug("Fetched {} events from page {}", pageResponse.content().size(), page);

            // Check if there are more pages
            hasMorePages = page < pageResponse.totalPages() - 1;
            page++;
        }

        return allEvents;
    }

    /**
     * Fetches all bookings for the given eventIds from booking-service
     *
     * @param eventIds List of event IDs
     * @return List of all bookings
     */
    private List<BookingResponse> fetchBookingsByEventIds(List<String> eventIds) {
        if (eventIds == null || eventIds.isEmpty()) {
            logger.warn("No eventIds provided to fetch bookings");
            return new ArrayList<>();
        }

        // Build URL with eventIds as comma-separated query parameter
        String bookingUrl = UriComponentsBuilder.fromHttpUrl(bookingServiceUrl)
                .path("/api/bookings/by-events")
                .queryParam("eventIds", String.join(",", eventIds))
                .toUriString();

        logger.debug("Calling booking-service: {}", bookingUrl);

        ResponseEntity<List<BookingResponse>> response = restTemplate.exchange(
                bookingUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<BookingResponse>>() {}
        );

        if (response.getBody() == null) {
            logger.error("booking-service returned null response for eventIds: {}", eventIds);
            return new ArrayList<>();
        }

        return response.getBody();
    }
}

