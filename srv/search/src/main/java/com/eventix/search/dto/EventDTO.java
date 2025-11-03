package com.eventix.search.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EventDTO {
    private String eventId;
    private String title;
    private String category;
    private String city;
    private String venue;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String organizerId;
    private double popularityScore;
}