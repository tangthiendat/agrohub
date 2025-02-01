package com.ttdat.userservice.infrastructure.utils;

import com.ttdat.userservice.api.dto.request.PaginationParams;
import com.ttdat.userservice.api.dto.request.SortParams;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RequestParamsUtils {

    private RequestParamsUtils() {
    }

    public static PaginationParams getPaginationParams(Map<String, String> requestParams) {
        int page = Integer.parseInt(requestParams.getOrDefault("page", "1"));
        int pageSize = Integer.parseInt(requestParams.getOrDefault("pageSize", "10"));
        requestParams.remove("page");
        requestParams.remove("pageSize");
        return PaginationParams.builder()
                .page(page)
                .pageSize(pageSize)
                .build();
    }

    public static SortParams getSortParams(Map<String, String> requestParams) {
        String sortBy = requestParams.getOrDefault("sortBy", "createdAt");
        String direction = requestParams.getOrDefault("direction", "desc");
        requestParams.remove("sortBy");
        requestParams.remove("direction");
        return SortParams.builder()
                .sortBy(sortBy)
                .direction(direction)
                .build();
    }
}
