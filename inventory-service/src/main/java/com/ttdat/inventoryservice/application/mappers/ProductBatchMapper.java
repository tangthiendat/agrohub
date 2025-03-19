package com.ttdat.inventoryservice.application.mappers;

import com.ttdat.inventoryservice.api.dto.common.ProductBatchDTO;
import com.ttdat.inventoryservice.domain.entities.ProductBatch;
import com.ttdat.inventoryservice.domain.events.batch.ProductBatchCreatedEvent;
import com.ttdat.inventoryservice.domain.events.batch.ProductBatchUpdatedEvent;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {ProductBatchLocationMapper.class})
public interface ProductBatchMapper {
    @Mapping(target = "warehouse.warehouseId", source = "warehouseId")
    ProductBatch toEntity(ProductBatchCreatedEvent productBatchCreatedEvent);

    @Mapping(target = "product.productId", source = "productId")
    ProductBatchDTO toDTO(ProductBatch productBatch);

    @Mapping(target = "warehouse", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromEvent(@MappingTarget ProductBatch productBatch, ProductBatchUpdatedEvent productBatchUpdatedEvent);

    @AfterMapping
    default void setBatchIdForBatchLocations(@MappingTarget ProductBatch productBatch, ProductBatchUpdatedEvent productBatchUpdatedEvent) {
        if(productBatchUpdatedEvent.getBatchLocations() != null) {
            productBatch.getBatchLocations().forEach(batchLocation ->
                    batchLocation.setProductBatch(ProductBatch.builder().batchId(productBatch.getBatchId()).build()));
        }
    }
}
