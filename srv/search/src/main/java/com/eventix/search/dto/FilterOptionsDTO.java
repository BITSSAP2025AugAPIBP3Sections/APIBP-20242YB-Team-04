package com.eventix.search.dto;

import lombok.Data;
import java.util.List;

@Data
public class FilterOptionsDTO {
    private List<String> categories;
    private List<String> cities;
    private List<String> tags;
}
