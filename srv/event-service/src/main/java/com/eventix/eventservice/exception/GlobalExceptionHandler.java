package com.eventix.eventservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private String newErrorId() { return UUID.randomUUID().toString(); }

    private ApiError buildApiError(HttpStatus status, String message, String path, String errorId) {
        return new ApiError(Instant.now(), status.value(), status.getReasonPhrase(), message, path, errorId);
    }

    // Validation -> 422 + invalidFields
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        String id = newErrorId();
        String topMessage = "One or more fields are invalid. Correct the input and try again.";

        List<FieldErrorDTO> invalidFields = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(f -> new FieldErrorDTO(f.getField(),
                        StringUtils.hasText(f.getDefaultMessage()) ? f.getDefaultMessage() : "invalid"))
                .collect(Collectors.toList());

        ApiError apiError = buildApiError(HttpStatus.UNPROCESSABLE_ENTITY, topMessage, req.getRequestURI(), id);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", apiError);
        body.put("invalidFields", invalidFields);

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).contentType(MediaType.APPLICATION_JSON).body(body);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequest(BadRequestException ex, HttpServletRequest req) {
        String id = newErrorId();
        String message = "The request is invalid. Review the parameters and try again.";
        ApiError a = buildApiError(HttpStatus.BAD_REQUEST, message, req.getRequestURI(), id);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(a);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiError> handleUnauthorized(UnauthorizedException ex, HttpServletRequest req) {
        String id = newErrorId();
        String message = "Authentication is required to access this resource. Provide valid credentials.";
        ApiError a = buildApiError(HttpStatus.UNAUTHORIZED, message, req.getRequestURI(), id);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(a);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiError> handleForbidden(ForbiddenException ex, HttpServletRequest req) {
        String id = newErrorId();
        String message = "You do not have permission to access this resource. Check your access rights.";
        ApiError a = buildApiError(HttpStatus.FORBIDDEN, message, req.getRequestURI(), id);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(a);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(NotFoundException ex, HttpServletRequest req) {
        String id = newErrorId();
        String message = "The requested resource was not found. Verify the identifier and try again.";
        ApiError a = buildApiError(HttpStatus.NOT_FOUND, message, req.getRequestURI(), id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(a);
    }

    @ExceptionHandler(TooManyRequestsException.class)
    public ResponseEntity<ApiError> handleTooManyRequests(TooManyRequestsException ex, HttpServletRequest req) {
        String id = newErrorId();
        String message = "Rate limit exceeded. Reduce the request frequency and retry later.";
        ApiError a = buildApiError(HttpStatus.TOO_MANY_REQUESTS, message, req.getRequestURI(), id);
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(a);
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ApiError> handleServiceUnavailable(ServiceUnavailableException ex, HttpServletRequest req) {
        String id = newErrorId();
        String detail = ex.getMessage();
        String message = (detail != null && !detail.isBlank())
                ? detail + " Please retry the request later."
                : "A dependent service is unavailable. Please retry the request later.";
        ApiError a = buildApiError(HttpStatus.SERVICE_UNAVAILABLE, message, req.getRequestURI(), id);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(a);
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<ApiError> handleResourceAccess(ResourceAccessException ex, HttpServletRequest req) {
        String id = newErrorId();
        String message = "A dependent service is unavailable. Please retry the request later.";
        ApiError a = buildApiError(HttpStatus.SERVICE_UNAVAILABLE, message, req.getRequestURI(), id);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(a);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ApiError> handleHttpClientError(HttpClientErrorException ex, HttpServletRequest req) {
        String id = newErrorId();
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        String message = ex.getStatusText();
        if (message == null || message.isBlank()) {
            message = "The downstream request failed with status " + status.value() + ".";
        } else {
            message = message + " Review the request and retry if appropriate.";
        }
        ApiError a = buildApiError(status, message, req.getRequestURI(), id);
        return ResponseEntity.status(status).body(a);
    }

    // fallback 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAny(Exception ex, HttpServletRequest req) {
        String id = newErrorId();
        String message = "An unexpected error occurred. Try again later or contact support with the error ID.";
        ApiError a = buildApiError(HttpStatus.INTERNAL_SERVER_ERROR, message, req.getRequestURI(), id);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(a);
    }
}
