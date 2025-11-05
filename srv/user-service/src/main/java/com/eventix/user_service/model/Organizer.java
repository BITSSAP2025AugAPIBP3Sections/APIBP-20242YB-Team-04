package com.eventix.user_service.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;

@Entity
@Data
public class Organizer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String organizationName;
    private String contactEmail;
    private String contactPhone;
    @OneToMany(mappedBy = "organizerId")
    private Set<User> users;
}
