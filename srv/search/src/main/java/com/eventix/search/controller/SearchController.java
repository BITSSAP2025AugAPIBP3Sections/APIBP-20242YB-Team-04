package com.eventix.search.controller;

import com.eventix.search.dto.*;
import com.eventix.search.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @Operation(summary = "Search events")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "The request is invalid. Review the parameters and try again."),
        @ApiResponse(responseCode = "422", description = "One or more fields are invalid. Correct the input and try again."),
        @ApiResponse(responseCode = "429", description = "Rate limit exceeded. Reduce the request frequency and retry later."),
        @ApiResponse(responseCode = "503", description = "A dependent service is unavailable. Please retry later."),
        @ApiResponse(responseCode = "500", description = "An unexpected error occurred. Try again later.")
    })
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

    @Operation(summary = "Get trending events")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "The request is invalid. Review the parameters and try again."),
        @ApiResponse(responseCode = "404", description = "The requested resource was not found."),
        @ApiResponse(responseCode = "429", description = "Rate limit exceeded. Reduce the request frequency and retry later."),
        @ApiResponse(responseCode = "503", description = "A dependent service is unavailable. Please retry later."),
        @ApiResponse(responseCode = "500", description = "An unexpected error occurred. Try again later.")
    })
    @GetMapping("/trending")
    public List<EventDTO> getTrendingEvents(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "10") int limit) {
        return searchService.getTrendingEvents(city, category, limit);
    }

    @Operation(summary = "Get calendar events")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "The request is invalid. Review the parameters and try again."),
        @ApiResponse(responseCode = "404", description = "The requested resource was not found."),
        @ApiResponse(responseCode = "503", description = "A dependent service is unavailable. Please retry later."),
        @ApiResponse(responseCode = "500", description = "An unexpected error occurred. Try again later.")
    })
    @GetMapping("/calendar")
    public CalenderEventDTO getEventsByCalendar(
            @RequestParam(required = false) String month,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String category) {

        if (month == null || month.isEmpty()) {
            month = java.time.LocalDate.now().getMonth().toString();
        }

        return searchService.getEventsByCalendar(month, city, category);
    }

    @Operation(summary = "Get available filter options")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "503", description = "A dependent service is unavailable. Please retry later."),
        @ApiResponse(responseCode = "500", description = "An unexpected error occurred. Try again later.")
    })
    @GetMapping("/filters")
    public FilterOptionsDTO getAvailableFilters() {
        return searchService.getAvailableFilters();
    }

    @Operation(summary = "Get suggestions for query")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "The request is invalid. Review the parameters and try again."),
        @ApiResponse(responseCode = "503", description = "A dependent service is unavailable. Please retry later."),
        @ApiResponse(responseCode = "500", description = "An unexpected error occurred. Try again later.")
    })
    @GetMapping("/suggest")
    public List<String> getSuggestions(
            @RequestParam String q,
            @RequestParam(defaultValue = "event") String type) {
        return searchService.getSuggestions(q, type);
    }

    @Operation(summary = "Get events for map")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "The request is invalid. Review the parameters and try again."),
        @ApiResponse(responseCode = "503", description = "A dependent service is unavailable. Please retry later."),
        @ApiResponse(responseCode = "500", description = "An unexpected error occurred. Try again later.")
    })
    @GetMapping("/map")
    public List<EventDTO> getMapEvents(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam(defaultValue = "10") double radius,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String date) {
        return searchService.getMapEvents(lat, lon, radius, category, date);
    }

    @Operation(summary = "Get recent events")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "The request is invalid. Review the parameters and try again."),
        @ApiResponse(responseCode = "503", description = "A dependent service is unavailable. Please retry later."),
        @ApiResponse(responseCode = "500", description = "An unexpected error occurred. Try again later.")
    })
    @GetMapping("/recent")
    public List<EventDTO> getRecentEvents(
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(required = false) String city) {
        return searchService.getRecentEvents(limit, city);
    }
}
