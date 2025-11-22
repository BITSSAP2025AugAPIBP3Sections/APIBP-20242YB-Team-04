package com.eventix.booking.dto;

import lombok.Data;
import java.util.UUID;
import java.time.ZonedDateTime;

@Data
public class EventResponse {
    private UUID id;
    private String title;
    private String city;
    private String category;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
}