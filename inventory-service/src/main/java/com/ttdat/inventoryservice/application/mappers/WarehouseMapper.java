package com.ttdat.inventoryservice.application.mappers;

import com.ttdat.inventoryservice.api.dto.common.WarehouseDTO;
import com.ttdat.inventoryservice.domain.entities.Warehouse;
import com.ttdat.inventoryservice.domain.events.WarehouseCreatedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WarehouseMapper extends EntityMapper<WarehouseDTO, Warehouse> {
    Warehouse toEntity(WarehouseCreatedEvent warehouseCreatedEvent);
}
