package com.eventix.user_service.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class UtilityController {
    @GetMapping("/health")
    public String health() {
        return "OK";
    }

    @GetMapping("/version")
    public String version() {
        return "Eventix User Service v0.0.1";
    }
}
