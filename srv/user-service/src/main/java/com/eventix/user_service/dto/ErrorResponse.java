package com.eventix.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private Instant timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private String errorId;
    
    public ErrorResponse(String error, String message, int status) {
        this.timestamp = Instant.now();
        this.error = error;
        this.message = message;
        this.status = status;
    }
    
    public ErrorResponse(String error, String message, int status, String path, String errorId) {
        this.timestamp = Instant.now();
        this.error = error;
        this.message = message;
        this.status = status;
        this.path = path;
        this.errorId = errorId;
    }
}
