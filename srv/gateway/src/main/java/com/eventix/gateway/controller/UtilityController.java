package com.eventix.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UtilityController {

    @GetMapping("/gateway/health")
    public String health() {
        return "API Gateway is running";
    }

    @GetMapping("/gateway/version")
    public String version() {
        return "Eventix API Gateway v0.0.1";
    }
}

