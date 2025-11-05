package com.eventix.eventservice.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class EventRequest {
    @NotBlank
    private String title;

    private String description;

    @NotBlank
    private String organizerId;

    private String category;
    private String city;
    private String venue;

    private Double latitude;
    private Double longitude;

    @NotNull
    private ZonedDateTime startTime;

    @NotNull
    private ZonedDateTime endTime;

    @Min(0)
    private Integer capacity = 0;

}
