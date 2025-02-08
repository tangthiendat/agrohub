package com.ttdat.productservice.application.handlers.query;

import com.ttdat.core.api.dto.response.PaginationMeta;
import com.ttdat.productservice.api.dto.response.UnitPageResult;
import com.ttdat.productservice.application.mappers.UnitMapper;
import com.ttdat.productservice.application.queries.GetUnitPageQuery;
import com.ttdat.productservice.domain.entities.Unit;
import com.ttdat.productservice.domain.repositories.UnitRepository;
import com.ttdat.productservice.infrastructure.utils.FilterUtils;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UnitQueryHandler {
    private final UnitRepository unitRepository;
    private final UnitMapper unitMapper;

    @QueryHandler
    public UnitPageResult handle(GetUnitPageQuery getUnitPageQuery) {
        List<Sort.Order> sortOrders = FilterUtils.toSortOrders(getUnitPageQuery.getSortParams());
        int page = getUnitPageQuery.getPaginationParams().getPage();
        int pageSize = getUnitPageQuery.getPaginationParams().getPageSize();
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(sortOrders));
        org.springframework.data.domain.Page<Unit> unitPage = unitRepository.findAll(pageable);

        PaginationMeta paginationMeta = PaginationMeta.builder()
                .page(unitPage.getNumber() + 1)
                .pageSize(unitPage.getSize())
                .totalElements(unitPage.getTotalElements())
                .totalPages(unitPage.getTotalPages())
                .build();
        return UnitPageResult.builder()
                .meta(paginationMeta)
                .content(unitMapper.toDTOs(unitPage.getContent()))
                .build();
    }
}
