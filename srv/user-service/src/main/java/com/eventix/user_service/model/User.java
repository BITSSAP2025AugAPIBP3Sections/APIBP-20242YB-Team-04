package com.eventix.user_service.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String name;
    private boolean emailVerified;
    private boolean active = true;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;
    // For attendees, organizerId is null
    private Long organizerId;
}
