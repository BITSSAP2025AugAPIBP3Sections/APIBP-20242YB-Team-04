package com.eventix.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizerSummaryDTO {
    private String organizerId;
    private String organizerName;
    private String organizationName;
    private String contactEmail;
    private String contactPhone;
    private String city;
    private String profileImageUrl;
    private int totalActiveEvents;
    private int totalPastEvents;
}
