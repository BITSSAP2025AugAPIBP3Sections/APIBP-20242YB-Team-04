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
import java.util.UUID;
import java.util.function.Function;

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
        // Validate that the organizer exists in User Service
        if (!userServiceClient.validateUser(req.getOrganizerId())) {
            throw new BadRequestException("Invalid organizer ID: " + req.getOrganizerId());
        }
        
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
}
