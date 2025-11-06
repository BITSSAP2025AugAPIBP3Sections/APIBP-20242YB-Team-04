package com.eventix.search.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {
    private String eventId;
    private String title;
    private String category;
    private String city;
    private String venue;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private String organizerId;
    private double popularityScore;
}