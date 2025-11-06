package com.eventix.search.controller;

import com.eventix.search.dto.*;
import com.eventix.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/events")
    public SearchResponseDTO searchEvents(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "popularity") String sortBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        return searchService.searchEvents(q, city, category, startDate, endDate, sortBy, page, limit);
    }

    @GetMapping("/trending")
    public List<EventDTO> getTrendingEvents(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "10") int limit) {
        return searchService.getTrendingEvents(city, category, limit);
    }

    @GetMapping("/calendar")
    public CalenderEventDTO getEventsByCalendar(
        @RequestParam(required = false) String month,
        @RequestParam(required = false) String city,
        @RequestParam(required = false) String category) {

    
    if (month == null || month.isEmpty()) {
        month = java.time.LocalDate.now().getMonth().toString(); // e.g. "NOVEMBER"
    }

    return searchService.getEventsByCalendar(month, city, category);
}

    @GetMapping("/filters")
    public FilterOptionsDTO getAvailableFilters() {
        return searchService.getAvailableFilters();
    }

    @GetMapping("/suggest")
    public List<String> getSuggestions(
            @RequestParam String q,
            @RequestParam(defaultValue = "event") String type) {
        return searchService.getSuggestions(q, type);
    }

    @GetMapping("/map")
    public List<EventDTO> getMapEvents(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam(defaultValue = "10") double radius,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String date) {
        return searchService.getMapEvents(lat, lon, radius, category, date);
    }

    @GetMapping("/recent")
    public List<EventDTO> getRecentEvents(
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(required = false) String city) {
        return searchService.getRecentEvents(limit, city);
    }
}
