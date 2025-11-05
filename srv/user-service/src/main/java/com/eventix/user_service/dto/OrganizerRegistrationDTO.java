package com.eventix.user_service.dto;

import lombok.Data;

@Data
public class OrganizerRegistrationDTO {
    private String email;
    private String password;
    private String name;
    private String organizationName;
    private String contactPhone;
}
