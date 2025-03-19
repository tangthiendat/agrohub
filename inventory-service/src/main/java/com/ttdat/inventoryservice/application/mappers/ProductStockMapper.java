package com.ttdat.inventoryservice.application.mappers;

import com.ttdat.core.domain.events.ProductStockAddedEvent;
import com.ttdat.inventoryservice.api.dto.common.ProductStockDTO;
import com.ttdat.inventoryservice.domain.entities.ProductStock;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductStockMapper extends EntityMapper<ProductStockDTO, ProductStock> {
    @Mapping(target = "warehouse.warehouseId", source = "warehouseId")
    ProductStock toEntity(ProductStockAddedEvent productStockAddedEvent);

    @Override
    @Mapping(target = "product", ignore = true)
    ProductStockDTO toDTO(ProductStock entity);
}
