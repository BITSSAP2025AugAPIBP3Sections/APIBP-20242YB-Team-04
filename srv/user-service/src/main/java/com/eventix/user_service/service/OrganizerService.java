package com.eventix.user_service.service;

import com.eventix.user_service.model.Organizer;
import com.eventix.user_service.dto.OrganizerRegistrationDTO;
import java.util.Optional;

public interface OrganizerService {
    Organizer registerOrganizer(OrganizerRegistrationDTO dto);
    Optional<Organizer> getOrganizerById(Long id);

    java.util.List<Object> getEventsForOrganizer(Long organizerId);

    java.util.List<Object> getAttendeesForEvent(Long organizerId, Long eventId);

    Object getOrganizerSummary(Long organizerId);
}
