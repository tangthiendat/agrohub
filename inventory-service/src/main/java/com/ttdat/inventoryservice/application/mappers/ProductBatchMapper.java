package com.ttdat.inventoryservice.application.mappers;

import com.ttdat.inventoryservice.domain.entities.ProductBatch;
import com.ttdat.inventoryservice.domain.events.batch.ProductBatchCreatedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductBatchMapper {
    @Mapping(target = "warehouse.warehouseId", source = "warehouseId")
    ProductBatch toEntity(ProductBatchCreatedEvent productBatchCreatedEvent);
}
