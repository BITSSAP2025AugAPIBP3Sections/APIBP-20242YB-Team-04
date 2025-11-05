package com.eventix.eventservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "events")
public class Event {

    @Id
    @Column(updatable = false, nullable = false)
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false)
    private String organizerId;

    @Column
    private String category;

    @Column
    private String city;

    @Column
    private String venue;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Column(nullable = false)
    private ZonedDateTime startTime;

    @Column(nullable = false)
    private ZonedDateTime endTime;

    @Column(nullable = false)
    private Integer capacity = 0;

    @Column(nullable = false)
    private Integer seatsAvailable = 0;

    @Enumerated(EnumType.STRING)
    private EventStatus status = EventStatus.DRAFT;

}

