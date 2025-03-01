package com.ttdat.inventoryservice.application.handlers.query;

import com.ttdat.core.application.exceptions.ErrorCode;
import com.ttdat.core.application.exceptions.ResourceNotFoundException;
import com.ttdat.inventoryservice.api.dto.common.WarehouseDTO;
import com.ttdat.inventoryservice.api.dto.response.WarehousePageResult;
import com.ttdat.inventoryservice.application.mappers.WarehouseMapper;
import com.ttdat.inventoryservice.application.queries.warehouse.GetAllWarehouseQuery;
import com.ttdat.inventoryservice.application.queries.warehouse.GetCurrentUserWarehouseQuery;
import com.ttdat.inventoryservice.application.queries.warehouse.GetWarehouseByIdQuery;
import com.ttdat.inventoryservice.application.queries.warehouse.GetWarehousePageQuery;
import com.ttdat.inventoryservice.domain.entities.Warehouse;
import com.ttdat.inventoryservice.domain.repositories.WarehouseRepository;
import com.ttdat.inventoryservice.infrastructure.utils.PaginationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class WarehouseQueryHandler {
    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper warehouseMapper;

    @QueryHandler
    public WarehousePageResult handle(GetWarehousePageQuery getWarehousePageQuery) {
        Pageable pageable = PaginationUtils.getPageable(getWarehousePageQuery.getPaginationParams(), getWarehousePageQuery.getSortParams());
        Page<Warehouse> warehousePage = warehouseRepository.findAll(pageable);
        return WarehousePageResult.builder()
                .meta(PaginationUtils.getPaginationMeta(warehousePage))
                .content(warehouseMapper.toDTOList(warehousePage.getContent()))
                .build();
    }

    @QueryHandler
    public List<WarehouseDTO> handle(GetAllWarehouseQuery getAllWarehouseQuery) {
        List<Warehouse> warehouses = warehouseRepository.findAll();
        return warehouseMapper.toDTOList(warehouses);
    }

    @QueryHandler
    public WarehouseDTO getWarehouseById(GetCurrentUserWarehouseQuery getCurrentUserWarehouseQuery, QueryMessage<?,?> queryMessage) {
        Long warehouseId = (Long) queryMessage.getMetaData().get("warehouse_id");
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.WAREHOUSE_NOT_FOUND));
        return warehouseMapper.toDTO(warehouse);
    }
}
