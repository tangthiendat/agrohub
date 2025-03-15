package com.ttdat.inventoryservice.application.mappers;

import com.ttdat.inventoryservice.api.dto.common.ProductLocationDTO;
import com.ttdat.inventoryservice.domain.entities.ProductLocation;
import com.ttdat.inventoryservice.domain.events.location.ProductLocationCreatedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductLocationMapper extends EntityMapper<ProductLocationDTO, ProductLocation> {
    @Mapping(target = "warehouse.warehouseId", source = "warehouseId")
    ProductLocation toEntity(ProductLocationCreatedEvent productLocationCreatedEvent);
}
