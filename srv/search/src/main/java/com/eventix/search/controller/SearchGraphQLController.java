package com.eventix.search.controller;

import com.eventix.search.dto.EventDTO;
import org.springframework.security.core.Authentication;
import com.eventix.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SearchGraphQLController {

    private final SearchService searchService;

    @QueryMapping
    public List<EventDTO> searchEvents(
            @Argument String q,
            @Argument String city,
            @Argument String category,
            @Argument String startTime,
            @Argument String endTime,
            @Argument String sortBy,
            @Argument Integer page,
            @Argument Integer limit
    ) {
        return searchService.searchEvents(q, city, category, startTime, endTime, sortBy,
                page != null ? page : 1, limit != null ? limit : 10).getEvents();
    }

    @QueryMapping
    public List<EventDTO> personalizedEvents(
            @Argument Integer limit,
            @Argument String basedOn
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) auth.getPrincipal();

        return searchService.getPersonalizedEvents(
                String.valueOf(userId),
                limit != null ? limit : 10,
                basedOn
        );
    }

}

