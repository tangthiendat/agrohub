package com.ttdat.userservice.infrastructure.utils;

import com.ttdat.core.api.dto.request.FilterCriteria;
import com.ttdat.core.api.dto.request.SortParams;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FilterUtils {

    private FilterUtils() {
    }

    public static List<Sort.Order> toSortOrders(SortParams sortParams) {
        List<Sort.Order> sortOrders = new ArrayList<>();
        Sort.Order updatedAtOrder = new Sort.Order(Sort.Direction.fromString("desc"), "updatedAt");
        sortOrders.add(updatedAtOrder);
        if(sortParams != null) {
            Sort.Order currentSortOrder = new Sort.Order(Sort.Direction.fromString(sortParams.getDirection()), sortParams.getSortBy());
            sortOrders.add(currentSortOrder);
        }
        return sortOrders;
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
