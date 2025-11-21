package com.eventix.eventservice.exception;

public record FieldErrorDTO(
    String field,
    String message
) {}
