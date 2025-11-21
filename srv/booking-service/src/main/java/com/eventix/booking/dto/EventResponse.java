package com.eventix.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
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