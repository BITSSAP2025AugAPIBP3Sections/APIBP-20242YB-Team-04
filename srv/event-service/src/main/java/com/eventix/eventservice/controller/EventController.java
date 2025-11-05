package com.eventix.eventservice.controller;

import com.eventix.eventservice.dto.EventRequest;
import com.eventix.eventservice.dto.EventResponse;
import com.eventix.eventservice.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
@Tag(name = "Event Management", description = "Manage and search events in the Eventix platform")
public class EventController {

    @Autowired
    private EventService service;

    @Operation(summary = "Create a new event", description = "Organizers use this to create new volunteering or donation events.")
    @PostMapping
    public ResponseEntity<EventResponse> create(@Valid @RequestBody EventRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createEvent(req));
    }

    @Operation(summary = "Get event by ID", description = "Fetch event details by its unique identifier.")
    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getEvent(id));
    }

    @Operation(summary = "Update event", description = "Organizers can update event details.")
    @PutMapping("/{id}")
    public ResponseEntity<EventResponse> update(@PathVariable UUID id, @Valid @RequestBody EventRequest req) {
        return ResponseEntity.ok(service.updateEvent(id, req));
    }

    @Operation(summary = "Delete event", description = "Deletes or deactivates an event by ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search events", description = "Search events by filters like city, category, date range, and organizer.")
    @GetMapping("/search")
    public ResponseEntity<Page<EventResponse>> search(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime to,
            @RequestParam(required = false) String organizerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<EventResponse> p = service.search(city, category, from, to, organizerId, page, size, "startTime");
        return ResponseEntity.ok(p);
    }
}
