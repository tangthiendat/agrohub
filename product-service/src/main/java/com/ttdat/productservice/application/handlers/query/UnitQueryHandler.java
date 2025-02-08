package com.ttdat.productservice.application.handlers.query;

import com.ttdat.core.api.dto.response.PaginationMeta;
import com.ttdat.productservice.api.dto.common.UnitDTO;
import com.ttdat.productservice.api.dto.response.UnitPageResult;
import com.ttdat.productservice.application.mappers.UnitMapper;
import com.ttdat.productservice.application.queries.unit.GetAllUnitsQuery;
import com.ttdat.productservice.application.queries.unit.GetUnitPageQuery;
import com.ttdat.productservice.domain.entities.Unit;
import com.ttdat.productservice.domain.repositories.UnitRepository;
import com.ttdat.productservice.infrastructure.utils.PaginationUtils;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UnitQueryHandler {
    private final UnitRepository unitRepository;
    private final UnitMapper unitMapper;

    @QueryHandler
    public List<UnitDTO> handle(GetAllUnitsQuery getAllUnitsQuery) {
        return unitMapper.toDTOs(unitRepository.findAll());
    }

    @QueryHandler
    public UnitPageResult handle(GetUnitPageQuery getUnitPageQuery) {
        Pageable pageable = PaginationUtils.getPageable(getUnitPageQuery.getPaginationParams(), getUnitPageQuery.getSortParams());
        Page<Unit> unitPage = unitRepository.findAll(pageable);
        PaginationMeta paginationMeta = PaginationUtils.getPaginationMeta(unitPage);
        return UnitPageResult.builder()
                .meta(paginationMeta)
                .content(unitMapper.toDTOs(unitPage.getContent()))
                .build();
    }
}
