package com.eventix.search.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class CalenderEventDTO {
    private String month;
    private Map<String, List<EventDTO>> eventsByDate;
}
