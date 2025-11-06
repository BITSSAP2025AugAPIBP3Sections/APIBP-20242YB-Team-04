package com.eventix.booking.controller;

import com.eventix.booking.model.Booking;
import com.eventix.booking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/stats")
public class StatsController {

    private final BookingService svc;

    @Autowired
    public StatsController(BookingService svc) {
        this.svc = svc;
    }

    /**
     * GET /api/stats/trending
     * Returns top N bookings ranked by totalBookings (descending).
     */
    @GetMapping("/trending")
    public List<Booking> trending(
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit) {
        return svc.trending(limit);
    }

    /**
     * GET /api/stats/personalized
     * Returns personalized bookings based on userId (if provided),
     * otherwise returns general recommendations.
     */
    @GetMapping("/personalized")
    public List<Booking> personalized(
            @RequestParam(name = "userId", required = false) String userId,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit) {
        return svc.personalized(userId, limit);
    }

    /**
     * GET /api/stats/deprecated
     * Deprecated endpoint for backward compatibility.
     * Informs clients to use GraphQL instead.
     */
    @Deprecated(forRemoval = true)
    @GetMapping("/deprecated")
    public Map<String, Object> deprecatedStats() {
        return Map.of(
            "message", "This REST endpoint is deprecated. Please use GraphQL at POST /graphql.",
            "exampleQuery", "query { eventStats(eventId: 202) { eventId totalBookings recentBookings wishlistCount avgRating city category } }"
        );
    }
}
