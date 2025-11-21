package com.eventix.eventservice.service;

import com.eventix.eventservice.config.UserServiceClient;
import com.eventix.eventservice.dto.EventRequest;
import com.eventix.eventservice.dto.EventResponse;
import com.eventix.eventservice.exception.BadRequestException;
import com.eventix.eventservice.exception.NotFoundException;
import com.eventix.eventservice.model.Event;
import com.eventix.eventservice.model.EventStatus;
import com.eventix.eventservice.repository.EventRepository;
import com.eventix.eventservice.spec.EventSpecification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private EventRepository repository;

    @Autowired
    private UserServiceClient userServiceClient;

    private final Function<Event, EventResponse> toDto = e -> {
        EventResponse r = new EventResponse();
        r.setId(e.getId());
        r.setTitle(e.getTitle());
        r.setDescription(e.getDescription());
        r.setOrganizerId(e.getOrganizerId());
        r.setCategory(e.getCategory());
        r.setCity(e.getCity());
        r.setVenue(e.getVenue());
        r.setLatitude(e.getLatitude());
        r.setLongitude(e.getLongitude());
        r.setStartTime(e.getStartTime());
        r.setEndTime(e.getEndTime());
        r.setCapacity(e.getCapacity());
        r.setSeatsAvailable(e.getSeatsAvailable());
        r.setStatus(e.getStatus());
        return r;
    };

    @Transactional
    public EventResponse createEvent(EventRequest req) {
        // Validate date/time logic
        if (req.getEndTime().isBefore(req.getStartTime())) {
            throw new BadRequestException("End time must be after start time");
        }
        
        if (req.getStartTime().isBefore(ZonedDateTime.now())) {
            throw new BadRequestException("Start time cannot be in the past");
        }
        
        // Validate that the organizer exists in User Service
        // TODO: Re-enable when user service is properly integrated
        // if (!userServiceClient.validateUser(req.getOrganizerId())) {
        //     throw new BadRequestException("Invalid organizer ID: " + req.getOrganizerId());
        // }
        
        Event e = new Event();
        e.setTitle(req.getTitle());
        e.setDescription(req.getDescription());
        e.setOrganizerId(req.getOrganizerId());
        e.setCategory(req.getCategory());
        e.setCity(req.getCity());
        e.setVenue(req.getVenue());
        e.setLatitude(req.getLatitude());
        e.setLongitude(req.getLongitude());
        e.setStartTime(req.getStartTime());
        e.setEndTime(req.getEndTime());
        e.setCapacity(req.getCapacity() != null ? req.getCapacity() : 0);
        e.setSeatsAvailable(e.getCapacity());
        e.setStatus(EventStatus.PUBLISHED);
        Event saved = repository.save(e);
        return toDto.apply(saved);
    }

    @Transactional(readOnly = true)
    public EventResponse getEvent(UUID id) {
        Event e = repository.findById(id).orElseThrow(() -> new NotFoundException("Event not found: " + id));
        return toDto.apply(e);
    }

    @Transactional
    public EventResponse updateEvent(UUID id, EventRequest req) {
        Event e = repository.findById(id).orElseThrow(() -> new NotFoundException("Event not found: " + id));
        if (req.getTitle() != null) e.setTitle(req.getTitle());
        if (req.getDescription() != null) e.setDescription(req.getDescription());
        if (req.getCategory() != null) e.setCategory(req.getCategory());
        if (req.getCity() != null) e.setCity(req.getCity());
        if (req.getVenue() != null) e.setVenue(req.getVenue());
        if (req.getLatitude() != null) e.setLatitude(req.getLatitude());
        if (req.getLongitude() != null) e.setLongitude(req.getLongitude());
        if (req.getStartTime() != null) e.setStartTime(req.getStartTime());
        if (req.getEndTime() != null) e.setEndTime(req.getEndTime());
        if (req.getCapacity() != null) {
            int diff = req.getCapacity() - e.getCapacity();
            e.setCapacity(req.getCapacity());
            e.setSeatsAvailable(Math.max(0, e.getSeatsAvailable() + diff));
        }
        Event saved = repository.save(e);
        return toDto.apply(saved);
    }

    @Transactional
    public void deleteEvent(UUID id) {
        Event e = repository.findById(id).orElseThrow(() -> new NotFoundException("Event not found: " + id));
        repository.delete(e);
    }

    @Transactional(readOnly = true)
    public Page<EventResponse> getEventsByOrganizer(String organizerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "startTime"));
        Specification<Event> spec = EventSpecification.hasOrganizer(organizerId);
        Page<Event> events = repository.findAll(spec, pageable);
        return events.map(toDto);
    }

    @Transactional(readOnly = true)
    public Page<EventResponse> search(
            String city,
            String category,
            ZonedDateTime from,
            ZonedDateTime to,
            String organizerId,
            int page,
            int size,
            String sort
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sort));

        // ✅ Replaced deprecated Specification.where(...) with custom combine()
        Specification<Event> spec = EventSpecification.combine(
                EventSpecification.isPublished(),
                EventSpecification.hasCity(city),
                EventSpecification.hasCategory(category),
                EventSpecification.startsAfter(from),
                EventSpecification.endsBefore(to),
                EventSpecification.hasOrganizer(organizerId)
        );

        Page<Event> p = repository.findAll(spec, pageable);
        return p.map(toDto);
    }


    public List<EventResponse> getMapEvents(double lat, double lon, double radius,
                                            String category, String date) {

        // Basic example without geospatial DB:
        // Return events filtered by category AND approximate city-level match.
        Specification<Event> spec = EventSpecification.combine(
                EventSpecification.isPublished(),
                EventSpecification.hasCategory(category)
        );

        List<Event> events = repository.findAll(spec);

        // very naive radius filtering (replace with real geo logic later)
        return events.stream().map(toDto).toList();
    }


    public List<EventResponse> getRecentEvents(int limit, String city) {

        Specification<Event> spec = EventSpecification.combine(
                EventSpecification.isPublished(),
                EventSpecification.hasCity(city)
        );

        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        return repository.findAll(spec, pageable).stream()
                .map(toDto)
                .toList();
    }

    public Map<String, Object> getEventsByCalendar(String month, String city, String category) {

        Specification<Event> spec = EventSpecification.combine(
                EventSpecification.isPublished(),
                EventSpecification.hasCity(city),
                EventSpecification.hasCategory(category)
        );

        List<EventResponse> events = repository.findAll(spec).stream()
                .map(toDto)
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("month", month);
        response.put("events", events);

        return response;
    }

    public Map<String, Object> getFilterOptions() {
        List<Event> events = repository.findAll();

        Set<String> categories = events.stream()
                .map(Event::getCategory)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<String> cities = events.stream()
                .map(Event::getCity)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<String> tags = Collections.emptySet(); // expand when tags available

        Map<String, Object> map = new HashMap<>();
        map.put("categories", categories);
        map.put("cities", cities);
        map.put("tags", tags);

        return map;
    }

    public List<String> getSuggestions(String q, String type) {
        if (q == null || q.isBlank()) return List.of();

        List<Event> events = repository.findAll();
        return events.stream()
                .map(Event::getTitle)
                .filter(t -> t.toLowerCase().contains(q.toLowerCase()))
                .limit(10)
                .toList();
    }

}
