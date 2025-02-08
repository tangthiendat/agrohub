package com.ttdat.productservice.infrastructure.utils;

import com.ttdat.core.api.dto.request.FilterCriteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FilterUtils {

    private FilterUtils() {
    }

    public static List<FilterCriteria> getFilterCriteriaList(Map<String, String> filters, String key) {
        List<FilterCriteria> filterCriteriaList = new ArrayList<>();
        String value = filters.get(key);
        if(value != null && !value.isEmpty()) {
            String[] valueArr = value.split(",");
            for (String searchValue : valueArr) {
                FilterCriteria searchCriteria = FilterCriteria.builder()
                        .key(key)
                        .operation("=")
                        .value(searchValue)
                        .build();
                filterCriteriaList.add(searchCriteria);
            }
        }
        return filterCriteriaList;
    }

}
