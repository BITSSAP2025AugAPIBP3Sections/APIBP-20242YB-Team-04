package com.eventix.search.integration;

import org.springframework.stereotype.Component;
import com.eventix.search.dto.EventDTO;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Component
public class AuthIntegrationClient {

    public Map<String, List<String>> getUserInterests(String userId) {
        return Map.of(
            "U123", List.of("Environmental", "Education"),
            "U456", List.of("Animal Welfare", "Community Service"),
            "U789", List.of("Health", "Fundraising")
        );
    }

    ZoneId zone = ZoneId.of("Asia/Kolkata");

    public List<EventDTO> getUserInterests(String userId, int limit, String basedOn) {
        return List.of(
            new EventDTO(
                "EVT201",
                "Beach Cleanup Drive",
                "Environmental",
                "Mumbai",
                "Juhu Beach",
                ZonedDateTime.now(zone).plusDays(2),
                ZonedDateTime.now(zone).plusDays(2).plusHours(3),
                "ORG001",
                95.0
            ),
            new EventDTO(
                "EVT202",
                "Teach a Child – Weekend Literacy Program",
                "Education",
                "Delhi",
                "Nehru Nagar Community Center",
                ZonedDateTime.now(zone).plusDays(5),
                ZonedDateTime.now(zone).plusDays(5).plusHours(4),
                "ORG014",
                91.5
            ),
            new EventDTO(
                "EVT203",
                "Animal Shelter Volunteering",
                "Animal Welfare",
                "Bangalore",
                "JP Nagar Shelter",
                ZonedDateTime.now(zone).plusDays(7),
                ZonedDateTime.now(zone).plusDays(7).plusHours(6),
                "ORG032",
                88.0
            ),
            new EventDTO(
                "EVT204",
                "Community Tree Plantation",
                "Environmental",
                "Pune",
                "Aundh Park",
                ZonedDateTime.now(zone).plusDays(10),
                ZonedDateTime.now(zone).plusDays(10).plusHours(5),
                "ORG021",
                86.0
            ),
            new EventDTO(
                "EVT205",
                "Health Awareness Marathon",
                "Health",
                "Chennai",
                "Marina Beach Road",
                ZonedDateTime.now(zone).plusDays(15),
                ZonedDateTime.now(zone).plusDays(15).plusHours(3),
                "ORG099",
                92.0
            )
        );
    }
}
