package com.eventix.search.controller;

import com.eventix.search.dto.EventDTO;
import com.eventix.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
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
            @Argument String startDate,
            @Argument String endDate,
            @Argument String sortBy,
            @Argument Integer page,
            @Argument Integer limit
    ) {
        return searchService.searchEvents(q, city, category, startDate, endDate, sortBy,
                page != null ? page : 1, limit != null ? limit : 10).getEvents();
    }

    @QueryMapping
    public List<EventDTO> personalizedEvents(
            @Argument String userId,
            @Argument Integer limit,
            @Argument String basedOn
    ) {
        return searchService.getPersonalizedEvents(userId, limit != null ? limit : 10, basedOn);
    }
}

