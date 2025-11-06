package com.eventix.Notification.service;

import com.eventix.Notification.dto.DashboardStatsDTO;
import com.eventix.Notification.external.BookingStatsDTO;
import com.eventix.Notification.external.EventStatsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Dashboard Service - ROLE 2: Data Aggregation
 *
 * This service "pulls" data from other microservices:
 * 1. Calls booking-service to get booking statistics
 * 2. Calls event-service to get event statistics
 * 3. Combines the responses into a single DashboardStatsDTO
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
     * Makes HTTP calls to booking-service and event-service
     *
     * @param organizerId The organizer's unique identifier
     * @return Combined dashboard statistics
     * @throws RuntimeException if either service call fails
     */
    public DashboardStatsDTO getOrganizerStats(String organizerId) {

        logger.info("Fetching dashboard stats for organizer: {}", organizerId);

        try {
            // 1. Get booking statistics from booking-service
            String bookingStatsUrl = buildUrl(bookingServiceUrl, "/api/bookings/stats", organizerId);
            logger.debug("Calling booking-service: {}", bookingStatsUrl);

            BookingStatsDTO bookingStats = restTemplate.getForObject(bookingStatsUrl, BookingStatsDTO.class);

            if (bookingStats == null) {
                logger.error("booking-service returned null response");
                throw new RuntimeException("Failed to fetch booking stats");
            }

            logger.info("Booking stats received: {} bookings, {} attendees, ${} revenue",
                bookingStats.totalBookings(),
                bookingStats.confirmedAttendees(),
                bookingStats.totalRevenue());

            // 2. Get event statistics from event-service
            String eventStatsUrl = buildUrl(eventServiceUrl, "/api/events/stats", organizerId);
            logger.debug("Calling event-service: {}", eventStatsUrl);

            EventStatsDTO eventStats = restTemplate.getForObject(eventStatsUrl, EventStatsDTO.class);

            if (eventStats == null) {
                logger.error("event-service returned null response");
                throw new RuntimeException("Failed to fetch event stats");
            }

            logger.info("Event stats received: {} total events, {} published",
                eventStats.totalEvents(),
                eventStats.publishedEvents());

            // 3. Combine both responses into one DTO
            DashboardStatsDTO combinedStats = new DashboardStatsDTO(
                eventStats.totalEvents(),
                eventStats.publishedEvents(),
                bookingStats.totalBookings(),
                bookingStats.confirmedAttendees(),
                bookingStats.totalRevenue()
            );

            logger.info("Dashboard stats aggregation complete for organizer: {}", organizerId);
            return combinedStats;

        } catch (Exception e) {
            logger.error("Failed to fetch dashboard stats for organizer {}: {}", organizerId, e.getMessage(), e);
            throw new RuntimeException("Failed to aggregate dashboard statistics: " + e.getMessage(), e);
        }
    }

    /**
     * Helper method to build URLs with query parameters
     */
    private String buildUrl(String baseUrl, String path, String organizerId) {
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path(path)
                .queryParam("organizerId", organizerId)
                .toUriString();
    }
}

