package com.eventix.user_service.graphql;

import com.eventix.user_service.service.OrganizerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class EventAttendeesQuery {
    @Autowired
    private OrganizerService organizerService;

    @QueryMapping
    public List<Object> eventAttendees(@Argument Long organizerId, @Argument Long eventId) {
        return organizerService.getAttendeesForEvent(organizerId, eventId);
    }
}
