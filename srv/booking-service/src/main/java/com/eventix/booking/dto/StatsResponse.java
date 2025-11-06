package com.eventix.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatsResponse {

    private Integer totalRecords;
    private Integer confirmed;
    private Double avgRating;
    private List<CityCount> byCity;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CityCount {
        private String city;
        private Integer count;
    }
}
