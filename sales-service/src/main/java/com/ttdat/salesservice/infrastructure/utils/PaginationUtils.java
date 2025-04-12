package com.ttdat.salesservice.infrastructure.utils;

import com.ttdat.core.api.dto.request.PaginationParams;
import com.ttdat.core.api.dto.request.SortParams;
import com.ttdat.core.api.dto.response.PaginationMeta;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class PaginationUtils {

    private PaginationUtils() {
    }

    public static List<Sort.Order> getSortOrders(SortParams sortParams) {
        List<Sort.Order> sortOrders = new ArrayList<>();
        if(sortParams != null) {
            Sort.Order currentSortOrder = new Sort.Order(Sort.Direction.fromString(sortParams.getDirection()), sortParams.getSortBy());
            sortOrders.add(currentSortOrder);
        }
        Sort.Order updatedAtOrder = new Sort.Order(Sort.Direction.fromString("desc"), "updatedAt");
        sortOrders.add(updatedAtOrder);
        return sortOrders;
    }

    public static Pageable getPageable(PaginationParams paginationParams, SortParams sortParams) {
        List<Sort.Order> sortOrders = getSortOrders(sortParams);
        int page = paginationParams.getPage();
        int pageSize = paginationParams.getPageSize();
        return PageRequest.of(page - 1, pageSize, Sort.by(sortOrders));
    }

    public static PaginationMeta getPaginationMeta(org.springframework.data.domain.Page<?> page) {
        return PaginationMeta.builder()
                .page(page.getNumber() + 1)
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}
