package com.eventix.search.dto;

import lombok.Data;

@Data
public class SearchRequestDTO {
    private String q;
    private String city;
    private String category;
    private String startDate;
    private String endDate;
    private String sortBy;
    private int page = 1;
    private int limit = 10;
    
}
