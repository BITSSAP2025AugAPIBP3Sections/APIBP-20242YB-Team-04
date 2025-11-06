package com.eventix.user_service.controller;

import com.eventix.user_service.dto.OrganizerRegistrationDTO;
import com.eventix.user_service.model.Organizer;
import com.eventix.user_service.service.OrganizerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/organizer")
public class OrganizerController {
    @Autowired
    private org.springframework.web.client.RestTemplate restTemplate;
    @Autowired
    private OrganizerService organizerService;

    @PostMapping("/register")
    public Organizer registerOrganizer(@RequestBody OrganizerRegistrationDTO dto) {
        return organizerService.registerOrganizer(dto);
    }

    // Dashboard endpoints (stubs, to be implemented)
    @GetMapping("/{organizerId}/events")
    public Object getEvents(@PathVariable Long organizerId) {
        // Example: Call Event Service REST endpoint
        String eventServiceUrl = "http://event-service/events?organizerId=" + organizerId;
        Object events = restTemplate.getForObject(eventServiceUrl, Object.class);
        return events;
    }

    @GetMapping("/{organizerId}/events/{eventId}/attendees")
    public Object getAttendees(@PathVariable Long organizerId, @PathVariable Long eventId) {
        // Example: Call Booking/Attendee Service REST endpoint
        String bookingServiceUrl = "http://booking-service/bookings?organizerId=" + organizerId + "&eventId=" + eventId;
        Object attendees = restTemplate.getForObject(bookingServiceUrl, Object.class);
        return attendees;
    }

    @GetMapping("/{organizerId}/summary")
    public Object getSummary(@PathVariable Long organizerId) {
        // Example: Call external summary/statistics service
        String summaryServiceUrl = "http://dashboard-service/summary?organizerId=" + organizerId;
        Object summary = restTemplate.getForObject(summaryServiceUrl, Object.class);
        return summary;
    }
}
