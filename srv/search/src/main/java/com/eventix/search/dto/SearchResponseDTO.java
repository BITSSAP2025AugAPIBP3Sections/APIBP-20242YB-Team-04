package com.eventix.search.dto;

import lombok.Data;
import java.util.List;

@Data
public class SearchResponseDTO {
    private List<EventDTO> events;
    private int page;
    private int limit;
    private long totalResults;
    
    public SearchResponseDTO(List<EventDTO> events) {
        this.events = events;
    }
}
