package com.eventix.booking.clients;

import com.eventix.booking.dto.EventResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "event-service", url = "http://localhost:8083")
public interface EventClient {

    @GetMapping("/api/v1/events/{id}")
    EventResponse getEvent(@PathVariable("id") UUID id);
}

