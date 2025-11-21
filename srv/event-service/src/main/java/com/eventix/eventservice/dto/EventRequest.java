package com.eventix.eventservice.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class EventRequest {
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 200, message = "Title must be between 3 and 200 characters")
    private String title;

    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;

    @NotBlank(message = "Organizer ID is required")
    private String organizerId;

    private String category;
    private String city;
    private String venue;

    private Double latitude;
    private Double longitude;

    @NotNull(message = "Start time is required")
    private ZonedDateTime startTime;

    @NotNull(message = "End time is required")
    private ZonedDateTime endTime;

    @Min(value = 0, message = "Capacity cannot be negative")
    @Max(value = 100000, message = "Capacity cannot exceed 100,000")
    private Integer capacity = 0;

}
