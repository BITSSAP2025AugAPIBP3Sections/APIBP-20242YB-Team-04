package com.eventix.booking.controller;

import com.eventix.booking.model.Booking;
import com.eventix.booking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/bookings")
@Tag(name = "Bookings", description = "Operations related to event bookings")
public class BookingController {

    private final BookingService svc;

    @Autowired
    public BookingController(BookingService svc) {
        this.svc = svc;
    }

    @Operation(summary = "Create a new booking", description = "Create a new booking. Use async=true to create in background.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Booking created successfully"),
            @ApiResponse(responseCode = "202", description = "Booking creation accepted (async)"),
            @ApiResponse(responseCode = "400", description = "Invalid booking payload")
    })
    @PostMapping
    public ResponseEntity<?> createBooking(
            @Parameter(description = "Create booking asynchronously if true", required = false)
            @RequestParam(name = "async", required = false, defaultValue = "false") boolean async,
            @Parameter(description = "Booking object to create", required = true)
            @RequestBody Booking booking) {

        if (booking == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid booking payload"));
        }

        if (async) {
            CompletableFuture<Booking> fut = svc.createBookingAsync(booking);
            String id = booking.getId();
            URI location = URI.create("/bookings/" + id);

            return ResponseEntity.accepted()
                    .location(location)
                    .body(Map.of(
                            "id", id,
                            "status", "PENDING",
                            "location", location.toString()
                    ));
        }

        Booking created = svc.createBooking(booking);   // <-- Calls EventClient internally
        URI location = URI.create("/bookings/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "Get booking by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking found"),
            @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getBooking(
            @Parameter(description = "Booking ID to retrieve", required = true)
            @PathVariable String id) {
        return svc.getBooking(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(Map.of("error", "Booking not found", "id", id)));
    }

    @Operation(summary = "List all bookings or filter by userId")
    @GetMapping
    public ResponseEntity<List<Booking>> listBookings(
            @Parameter(description = "Filter bookings by userId", required = false)
            @RequestParam(name = "userId", required = false) String userId) {
        List<Booking> results = (userId != null && !userId.isBlank())
                ? svc.getBookingsByUser(userId)
                : svc.getAllBookings();
        return ResponseEntity.ok(results);
    }

    @Operation(summary = "Get bookings by event IDs", description = "Fetch bookings for one or more event IDs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bookings retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters")
    })
    @GetMapping("/by-events")
    public ResponseEntity<?> getBookingsByEventIds(
            @Parameter(description = "Single event ID to filter bookings", required = false)
            @RequestParam(name = "eventId", required = false) String eventId,
            @Parameter(description = "Multiple event IDs to filter bookings (comma-separated)", required = false)
            @RequestParam(name = "eventIds", required = false) List<String> eventIds) {

        if (eventId != null && !eventId.isBlank()) {
            List<Booking> results = svc.getBookingsByEventId(eventId);
            return ResponseEntity.ok(results);
        } else if (eventIds != null && !eventIds.isEmpty()) {
            List<Booking> results = svc.getBookingsByEventIds(eventIds);
            return ResponseEntity.ok(results);
        } else {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Either eventId or eventIds parameter is required"));
        }
    }

    @Operation(summary = "Get booking statistics")
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        return ResponseEntity.ok(svc.stats());
    }

    @Operation(summary = "Get trending bookings", description = "Returns the most trending bookings")
    @GetMapping("/trending")
    public ResponseEntity<List<Booking>> getTrending(
            @Parameter(description = "Maximum number of trending bookings", required = false)
            @RequestParam(name = "limit", defaultValue = "5") int limit) {
        return ResponseEntity.ok(svc.trending(limit));
    }

    @Operation(summary = "Get personalized bookings for a user")
    @GetMapping("/personalized")
    public ResponseEntity<List<Booking>> getPersonalized(
            @Parameter(description = "User ID for personalized results", required = false)
            @RequestParam(name = "userId", required = false) String userId,
            @Parameter(description = "Limit number of results", required = false)
            @RequestParam(name = "limit", defaultValue = "5") int limit) {
        return ResponseEntity.ok(svc.personalized(userId, limit));
    }

    @Operation(summary = "Delete booking by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Booking deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Booking not found"),
            @ApiResponse(responseCode = "500", description = "Failed to delete booking")
    })
    @DeleteMapping("/{id}")
public ResponseEntity<?> deleteBooking(@PathVariable String id, HttpServletRequest req) {

    String role = (String) req.getAttribute("role");

    if (!"ADMIN".equals(role) && !"ORGANIZER".equals(role)) {
        return ResponseEntity.status(403)
                .body(Map.of("error", "You do not have permission to delete bookings"));
    }

    try {
        svc.deleteBooking(id);
        return ResponseEntity.noContent().build();
    } catch (NoSuchElementException e) {
        return ResponseEntity.status(404)
                .body(Map.of("error", "Booking not found", "id", id));
    }
}

}
