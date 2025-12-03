package com.eventix.search.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventDTO {
    private String id;
    private String title;
    private String category;
    private String city;
    private String venue;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    private String organizerId;
    private double popularityScore;
}