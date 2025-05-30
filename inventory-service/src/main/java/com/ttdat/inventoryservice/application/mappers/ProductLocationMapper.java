package com.ttdat.inventoryservice.application.mappers;

import com.ttdat.inventoryservice.api.dto.common.ProductLocationDTO;
import com.ttdat.inventoryservice.domain.entities.ProductLocation;
import com.ttdat.inventoryservice.domain.events.location.ProductLocationCreatedEvent;
import com.ttdat.inventoryservice.domain.events.location.ProductLocationUpdatedEvent;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductLocationMapper extends EntityMapper<ProductLocationDTO, ProductLocation> {
    @Override
    @Mapping(target = "warehouseId", source = "warehouse.warehouseId")
    ProductLocationDTO toDTO(ProductLocation entity);

    @Mapping(target = "warehouse.warehouseId", source = "warehouseId")
    ProductLocation toEntity(ProductLocationCreatedEvent productLocationCreatedEvent);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromEvent(@MappingTarget ProductLocation productLocation, ProductLocationUpdatedEvent productLocationUpdatedEvent);
}
