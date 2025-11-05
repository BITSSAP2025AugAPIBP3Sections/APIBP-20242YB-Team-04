package com.eventix.eventservice.dto;

import com.eventix.eventservice.model.EventStatus;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class EventResponse {
    private String id;
    private String title;
    private String description;
    private String organizerId;
    private String category;
    private String city;
    private String venue;
    private Double latitude;
    private Double longitude;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    private Integer capacity;
    private Integer seatsAvailable;
    private EventStatus status;

}
