package com.eventix.user_service.graphql;

import com.eventix.user_service.service.OrganizerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class OrganizerQuery {
    @Autowired
    private OrganizerService organizerService;

    @QueryMapping
    public List<Object> organizerEvents(@Argument Long organizerId) {
        // Replace Object with Event if you have an Event model
        return organizerService.getEventsForOrganizer(organizerId);
    }
}
