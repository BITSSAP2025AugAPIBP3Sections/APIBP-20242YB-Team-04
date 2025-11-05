package com.eventix.user_service.graphql;

import com.eventix.user_service.service.OrganizerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.stereotype.Controller;

@Controller
public class OrganizerSummaryQuery {
    @Autowired
    private OrganizerService organizerService;

    @QueryMapping
    public Object organizerSummary(@Argument Long organizerId) {
        // TODO: Replace Object with Summary model and implement actual summary fetching logic
        return organizerService.getOrganizerSummary(organizerId);
    }
}
