package com.eventix.Notification.controller;

import com.eventix.Notification.dto.DashboardStatsDTO;
import com.eventix.Notification.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Dashboard Controller - ROLE 2: REST API for Frontend
 *
 * Exposes dashboard endpoints that aggregate data from multiple microservices
 * This is the API your frontend will call to display organizer statistics
 */
@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
@Tag(name = "Dashboard", description = "Organizer dashboard statistics endpoints")
public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    @Autowired
    private DashboardService dashboardService;

    @Operation(
        summary = "Get organizer dashboard statistics",
        description = "Retrieves aggregated statistics for an event organizer by pulling data from booking-service and event-service"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved dashboard statistics",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = DashboardStatsDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "503",
            description = "Service Unavailable - Dependent services are down",
            content = @Content
        )
    })
    @GetMapping("/organizer/{organizerId}/stats")
    public ResponseEntity<DashboardStatsDTO> getDashboardStats(
        @Parameter(description = "Unique identifier of the event organizer", example = "org-123")
        @PathVariable String organizerId
    ) {

        logger.info("Dashboard API called for organizer: {}", organizerId);

        try {
            // Call the service to aggregate data from other microservices
            DashboardStatsDTO stats = dashboardService.getOrganizerStats(organizerId);

            logger.info("Dashboard stats successfully retrieved for organizer: {}", organizerId);
            return ResponseEntity.ok(stats);

        } catch (Exception e) {
            // Handle errors (e.g., booking-service or event-service is down)
            logger.error("Failed to get dashboard stats for organizer {}: {}", organizerId, e.getMessage());

            // Return 503 Service Unavailable if dependent services are down
            return ResponseEntity.status(503).build();
        }
    }

    @Operation(
        summary = "Health check for dashboard API",
        description = "Returns the health status of the dashboard API"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Dashboard API is running"
    )
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Dashboard API is running!");
    }
}

