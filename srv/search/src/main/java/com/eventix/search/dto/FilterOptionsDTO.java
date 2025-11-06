package com.eventix.search.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterOptionsDTO {
    private List<String> categories;
    private List<String> cities;
    private List<String> tags;
}
