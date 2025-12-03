package com.eventix.booking.dto;

import lombok.Data;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.ZonedDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventResponse {
    private UUID id;
    private String title;
    private String venue;
    private String city;
    private String category;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
}