package com.ttdat.inventoryservice.application.mappers;

import com.ttdat.inventoryservice.api.dto.common.WarehouseDTO;
import com.ttdat.inventoryservice.domain.entities.Warehouse;
import com.ttdat.inventoryservice.domain.events.warehouse.WarehouseCreatedEvent;
import com.ttdat.inventoryservice.domain.events.warehouse.WarehouseUpdatedEvent;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WarehouseMapper extends EntityMapper<WarehouseDTO, Warehouse> {
    Warehouse toEntity(WarehouseCreatedEvent warehouseCreatedEvent);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromEvent(@MappingTarget Warehouse warehouse, WarehouseUpdatedEvent warehouseUpdatedEvent);
}
