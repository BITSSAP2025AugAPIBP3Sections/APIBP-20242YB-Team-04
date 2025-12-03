package com.eventix.search.dto;

import lombok.Data;

@Data
public class SearchRequestDTO {
    private String q;
    private String city;
    private String category;
    private String startTime;
    private String endTime;
    private String sortBy;
    private int page = 1;
    private int limit = 10;
    
}
