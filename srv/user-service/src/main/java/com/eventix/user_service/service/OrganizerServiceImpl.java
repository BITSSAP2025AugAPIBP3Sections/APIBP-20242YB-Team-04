package com.eventix.user_service.service;

import com.eventix.user_service.model.Organizer;
import com.eventix.user_service.dto.OrganizerRegistrationDTO;
import com.eventix.user_service.repository.OrganizerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class OrganizerServiceImpl implements OrganizerService {

    private static final Logger access = LogManager.getLogger("access");
    private static final Logger error  = LogManager.getLogger("error");
    private static final Logger debug  = LogManager.getLogger("debug");

    @Autowired
    private com.eventix.user_service.repository.UserRepository userRepository;

    @Autowired
    private com.eventix.user_service.repository.RoleRepository roleRepository;

    @Autowired
    private OrganizerRepository organizerRepository;

    @Override
    public Organizer registerOrganizer(OrganizerRegistrationDTO dto) {
        access.info("registerOrganizer called email={} orgName={}", dto.getEmail(), dto.getOrganizationName());
        debug.debug("registerOrganizer payload={}", dto);

        try {
            // Create organizer record
            Organizer organizer = new Organizer();
            organizer.setOrganizationName(dto.getOrganizationName());
            organizer.setContactEmail(dto.getEmail());
            organizer.setContactPhone(dto.getContactPhone());
            Organizer savedOrganizer = organizerRepository.save(organizer);
            access.info("Organizer created id={} orgName={}", savedOrganizer.getId(), savedOrganizer.getOrganizationName());

            // Create linked User account
            com.eventix.user_service.model.User user = new com.eventix.user_service.model.User();
            user.setEmail(dto.getEmail());
            user.setPassword(dto.getPassword()); // TODO: hash password

            String[] nameParts = dto.getName().split(" ", 2);
            user.setFirstName(nameParts[0]);
            user.setLastName(nameParts.length > 1 ? nameParts[1] : "");

            user.setEmailVerified(false);
            user.setActive(true);
            user.setOrganizerId(savedOrganizer.getId());

            // Assign organizer role
            var organizerRole = roleRepository.findByName("organizer").orElse(null);
            if (organizerRole != null) {
                java.util.Set<com.eventix.user_service.model.Role> roles = new java.util.HashSet<>();
                roles.add(organizerRole);
                user.setRoles(roles);
                debug.debug("Assigned organizer role to email={}", dto.getEmail());
            } else {
                error.error("Organizer role missing for new organizer email={}", dto.getEmail());
            }

            userRepository.save(user);
            access.info("Organizer user created userEmail={} orgId={}", dto.getEmail(), savedOrganizer.getId());

            return savedOrganizer;

        } catch (Exception e) {
            error.error("registerOrganizer failed email={} reason={}", dto.getEmail(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Optional<Organizer> getOrganizerById(Long id) {
        access.info("getOrganizerById called id={}", id);
        Optional<Organizer> found = organizerRepository.findById(id);

        if (found.isPresent()) {
            access.info("getOrganizerById found id={}", id);
        } else {
            debug.debug("getOrganizerById no organizer found id={}", id);
        }

        return found;
    }

    @Override
    public java.util.List<Object> getEventsForOrganizer(Long organizerId) {
        access.info("getEventsForOrganizer called organizerId={}", organizerId);
        // Real implementation later
        return java.util.Collections.emptyList();
    }

    @Override
    public java.util.List<Object> getAttendeesForEvent(Long organizerId, Long eventId) {
        access.info("getAttendeesForEvent called organizerId={} eventId={}", organizerId, eventId);
        // Real implementation later
        return java.util.Collections.emptyList();
    }

    @Override
    public Object getOrganizerSummary(Long organizerId) {
        access.info("getOrganizerSummary called organizerId={}", organizerId);
        // Real implementation later
        return null;
    }
}
