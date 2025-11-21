package com.eventix.user_service.service;

import com.eventix.user_service.model.Organizer;
import com.eventix.user_service.dto.OrganizerRegistrationDTO;
import com.eventix.user_service.repository.OrganizerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class OrganizerServiceImpl implements OrganizerService {
    @Autowired
    private com.eventix.user_service.repository.UserRepository userRepository;
    @Autowired
    private com.eventix.user_service.repository.RoleRepository roleRepository;
    @Autowired
    private OrganizerRepository organizerRepository;

    @Override
        public Organizer registerOrganizer(OrganizerRegistrationDTO dto) {
            Organizer organizer = new Organizer();
            organizer.setOrganizationName(dto.getOrganizationName());
            organizer.setContactEmail(dto.getEmail());
            organizer.setContactPhone(dto.getContactPhone());
            Organizer savedOrganizer = organizerRepository.save(organizer);

            // Create User for organizer
            com.eventix.user_service.model.User user = new com.eventix.user_service.model.User();
            user.setEmail(dto.getEmail());
            user.setPassword(dto.getPassword()); // TODO: hash password
            // Split name into first and last name
            String[] nameParts = dto.getName().split(" ", 2);
            user.setFirstName(nameParts[0]);
            user.setLastName(nameParts.length > 1 ? nameParts[1] : "");
            user.setEmailVerified(false);
            user.setActive(true);
            user.setOrganizerId(savedOrganizer.getId());
            // Assign 'organizer' role
            var organizerRole = roleRepository.findByName("organizer").orElse(null);
            if (organizerRole != null) {
                java.util.Set<com.eventix.user_service.model.Role> roles = new java.util.HashSet<>();
                roles.add(organizerRole);
                user.setRoles(roles);
            }
            userRepository.save(user);
            return savedOrganizer;
        }

    @Override
    public Optional<Organizer> getOrganizerById(Long id) {
        return organizerRepository.findById(id);
    }

    @Override
    public java.util.List<Object> getEventsForOrganizer(Long organizerId) {
        
        return java.util.Collections.emptyList();
    }

    @Override
    public java.util.List<Object> getAttendeesForEvent(Long organizerId, Long eventId) {
        
        return java.util.Collections.emptyList();
    }

    @Override
    public Object getOrganizerSummary(Long organizerId) {
       
        return null;
    }
}
