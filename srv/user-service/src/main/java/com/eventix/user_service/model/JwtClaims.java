package com.eventix.user_service.model;

import lombok.Data;

@Data
public class JwtClaims {
    private Long userId;
    private String role;
    private Long organizerId;
    private String firstName;
    private String lastName;
}
