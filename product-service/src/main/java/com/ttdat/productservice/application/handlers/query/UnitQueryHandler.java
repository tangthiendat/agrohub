package com.ttdat.productservice.application.handlers.query;

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
        return unitMapper.toDTOList(unitRepository.findAll());
    }

    @QueryHandler
    public UnitPageResult handle(GetUnitPageQuery getUnitPageQuery) {
        Pageable pageable = PaginationUtils.getPageable(getUnitPageQuery.getPaginationParams(), getUnitPageQuery.getSortParams());
        Page<Unit> unitPage = unitRepository.findAll(pageable);
        return UnitPageResult.builder()
                .meta(PaginationUtils.getPaginationMeta(unitPage))
                .content(unitMapper.toDTOList(unitPage.getContent()))
                .build();
    }
}
