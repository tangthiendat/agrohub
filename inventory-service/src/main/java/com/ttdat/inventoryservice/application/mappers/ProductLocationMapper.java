package com.ttdat.inventoryservice.application.mappers;

import com.ttdat.inventoryservice.api.dto.common.ProductLocationDTO;
import com.ttdat.inventoryservice.domain.entities.ProductLocation;
import com.ttdat.inventoryservice.domain.events.location.ProductLocationCreatedEvent;
import com.ttdat.inventoryservice.domain.events.location.ProductLocationUpdatedEvent;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductLocationMapper extends EntityMapper<ProductLocationDTO, ProductLocation> {
    @Mapping(target = "warehouse.warehouseId", source = "warehouseId")
    ProductLocation toEntity(ProductLocationCreatedEvent productLocationCreatedEvent);

    @Mapping(target = "warehouse.warehouseId", source = "warehouseId")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromEvent(@MappingTarget ProductLocation productLocation, ProductLocationUpdatedEvent productLocationUpdatedEvent);
}
