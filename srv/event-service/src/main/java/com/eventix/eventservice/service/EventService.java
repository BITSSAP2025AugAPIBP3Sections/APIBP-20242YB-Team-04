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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Month;
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

    private static final Logger accessLog = LoggerFactory.getLogger("access");
    private static final Logger errorLog  = LoggerFactory.getLogger("error");
    private static final Logger debugLog  = LoggerFactory.getLogger("debug");

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

    // ---------------------------- CREATE ------------------------------------
    @Transactional
    public EventResponse createEvent(EventRequest req) {
        accessLog.info("createEvent organizerId={} title={}", req.getOrganizerId(), req.getTitle());

        if (req.getEndTime().isBefore(req.getStartTime())) {
            errorLog.error("Invalid date range start={} end={}", req.getStartTime(), req.getEndTime());
            throw new BadRequestException("End time must be after start time");
        }

        if (req.getStartTime().isBefore(ZonedDateTime.now())) {
            errorLog.error("Start time is in the past: {}", req.getStartTime());
            throw new BadRequestException("Start time cannot be in the past");
        }

        if (!userServiceClient.validateUser(req.getOrganizerId())) {
            errorLog.error("Organizer validation failed organizerId={}", req.getOrganizerId());
            throw new BadRequestException("Invalid organizer ID: " + req.getOrganizerId());
        }

        debugLog.debug("Creating event in database…");

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
        accessLog.info("Event created id={}", saved.getId());
        return toDto.apply(saved);
    }

    // ---------------------------- GET ALL ------------------------------------
    @Transactional(readOnly = true)
    public Page<EventResponse> getAllEvents(int page, int size) {
        accessLog.info("getAllEvents page={} size={}", page, size);
        return repository.findAll(PageRequest.of(page, size, Sort.by("startTime").descending())).map(toDto);
    }

    // ---------------------------- GET ONE ------------------------------------
    @Transactional(readOnly = true)
    public EventResponse getEvent(UUID id) {
        accessLog.info("getEvent id={}", id);

        Event e = repository.findById(id).orElseThrow(() -> {
            errorLog.error("Event not found id={}", id);
            return new NotFoundException("Event not found: " + id);
        });

        return toDto.apply(e);
    }

    // ---------------------------- UPDATE --------------------------------------
    @Transactional
    public EventResponse updateEvent(UUID id, EventRequest req) {
        accessLog.info("updateEvent id={}", id);
        debugLog.debug("Update payload received");

        Event e = repository.findById(id).orElseThrow(() -> {
            errorLog.error("Event not found for update id={}", id);
            return new NotFoundException("Event not found: " + id);
        });

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

        Event updated = repository.save(e);
        accessLog.info("Event updated id={}", id);
        return toDto.apply(updated);
    }

    // ---------------------------- DELETE --------------------------------------
    @Transactional
    public void deleteEvent(UUID id) {
        accessLog.info("deleteEvent id={}", id);

        Event e = repository.findById(id).orElseThrow(() -> {
            errorLog.error("Event not found for delete id={}", id);
            return new NotFoundException("Event not found: " + id);
        });

        repository.delete(e);
        accessLog.info("Event deleted id={}", id);
    }

    // ---------------------------- SEARCH ----------------------------------------
    @Transactional(readOnly = true)
    public Page<EventResponse> search(String city, String category, ZonedDateTime from, ZonedDateTime to,
                                      String organizerId, int page, int size, String sort) {

        accessLog.info("search city={} category={} organizer={}", city, category, organizerId);
        debugLog.debug("Search pagination: page={} size={} sort={}", page, size, sort);

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).descending());

        Specification<Event> spec = EventSpecification.combine(
                EventSpecification.isPublished(),
                EventSpecification.hasCity(city),
                EventSpecification.hasCategory(category),
                EventSpecification.startsAfter(from),
                EventSpecification.endsBefore(to),
                EventSpecification.hasOrganizer(organizerId)
        );

        Page<Event> p = repository.findAll(spec, pageable);

        debugLog.debug("Search results returned count={}", p.getTotalElements());
        return p.map(toDto);
    }

    // ---------------------------- RECENT EVENTS ----------------------------------
    public List<EventResponse> getRecentEvents(int limit, String city) {
        accessLog.info("getRecentEvents limit={} city={}", limit, city);

        Specification<Event> spec = EventSpecification.combine(
                EventSpecification.isPublished(),
                EventSpecification.hasCity(city)
        );

        Pageable pageable = PageRequest.of(0, limit, Sort.by("createdAt").descending());

        List<EventResponse> list = repository.findAll(spec, pageable)
                .stream()
                .map(toDto)
                .toList();

        debugLog.debug("Recent events count={}", list.size());
        return list;
    }

    // ----------------------------- FILTER OPTIONS ---------------------------------
    public Map<String, Object> getFilterOptions() {
        accessLog.info("getFilterOptions called");

        List<Event> events = repository.findAll();

        Map<String, Object> map = new HashMap<>();
        map.put("categories", events.stream().map(Event::getCategory).filter(Objects::nonNull).collect(Collectors.toSet()));
        map.put("cities",     events.stream().map(Event::getCity).filter(Objects::nonNull).collect(Collectors.toSet()));
        map.put("tags",       Collections.emptySet());

        return map;
    }

    // ----------------------------- SUGGESTIONS ------------------------------------
    public List<String> getSuggestions(String q, String type) {
        accessLog.info("getSuggestions q={} type={}", q, type);

        if (q == null || q.isBlank()) return List.of();

        List<String> suggestions = repository.findAll()
                .stream()
                .map(Event::getTitle)
                .filter(t -> t.toLowerCase().contains(q.toLowerCase()))
                .limit(10)
                .toList();

        debugLog.debug("Suggestions total={}", suggestions.size());
        return suggestions;
    }

    // ----------------------------- GET EVENTS BY ORGANIZER -------------------------
    @Transactional(readOnly = true)
    public Page<EventResponse> getEventsByOrganizer(String organizerId, int page, int size) {
        accessLog.info("getEventsByOrganizer organizerId={} page={} size={}", organizerId, page, size);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("startTime").descending());
        Page<Event> events = repository.findByOrganizerId(organizerId, pageable);
        
        debugLog.debug("Events by organizer count={}", events.getTotalElements());
        return events.map(toDto);
    }

    // ----------------------------- MAP EVENTS (GEOSPATIAL) -------------------------
    public List<EventResponse> getMapEvents(double lat, double lon, double radius,
        String category, String date) {
accessLog.info("getMapEvents lat={} lon={} radius={} category={} date={}",
lat, lon, radius, category, date);

Specification<Event> spec = EventSpecification.combine(
EventSpecification.isPublished(),
EventSpecification.hasCategory(category)
);

// Fetch from DB based on category
List<Event> events = repository.findAll(spec);

List<EventResponse> filtered = events.stream()
.filter(e -> {
// 1) Date filter (optional)
if (date != null && !date.isBlank()) {
if (!e.getStartTime().toString().startsWith(date)) {
return false;
}
}

// 2) Geo-distance filter
double distance = haversine(
lat,
lon,
e.getLatitude(),
e.getLongitude()
);

return distance <= radius;
})
.map(toDto)
.toList();

debugLog.debug("Map events count={}", filtered.size());
return filtered;
}

private double haversine(double lat1, double lon1, Double lat2, Double lon2) {
    if (lat2 == null || lon2 == null) {
        return Double.MAX_VALUE; // event has no coordinates → auto exclude
    }

    final double R = 6371; // Radius of Earth in KM

    double dLat = Math.toRadians(lat2 - lat1);
    double dLon = Math.toRadians(lon2 - lon1);

    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
            + Math.cos(Math.toRadians(lat1))
            * Math.cos(Math.toRadians(lat2))
            * Math.sin(dLon / 2) * Math.sin(dLon / 2);

    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return R * c;
}

    // ----------------------------- CALENDAR EVENTS ---------------------------------
    public Map<String, Object> getEventsByCalendar(String month, String city, String category) {

        accessLog.info("getEventsByCalendar month={} city={} category={}", month, city, category);
    
        // Build base specification (published + optional city/category)
        Specification<Event> spec = EventSpecification.combine(
                EventSpecification.isPublished(),
                EventSpecification.hasCity(city),
                EventSpecification.hasCategory(category)
        );
    
        List<Event> events = repository.findAll(spec);
    
        // -------------------------------
        // SAFE MONTH FILTERING
        // -------------------------------
        Month targetMonth = null;
    
        if (month != null && !month.isBlank()) {
            try {
                targetMonth = Month.valueOf(month.trim().toUpperCase());
            } catch (Exception ex) {
                errorLog.error("Invalid month provided: {}", month);
                targetMonth = null; // Skip filtering instead of throwing 500
            }
        }
    
        Month finalTargetMonth = targetMonth;
    
        List<EventResponse> filtered = events.stream()
                .filter(e -> {
                    if (finalTargetMonth != null) {
                        return e.getStartTime().getMonth() == finalTargetMonth;
                    }
                    return true;
                })
                .map(toDto)
                .toList();
    
        // Prepare response
        Map<String, Object> response = new HashMap<>();
        response.put("month", month);
        response.put("events", filtered);
    
        debugLog.debug("Calendar events found={}", filtered.size());
    
        return response;
    }
    
}
