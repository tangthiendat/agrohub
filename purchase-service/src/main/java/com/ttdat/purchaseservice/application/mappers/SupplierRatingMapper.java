package com.ttdat.purchaseservice.application.mappers;

import com.ttdat.purchaseservice.api.dto.response.SupplierRatingDTO;
import com.ttdat.purchaseservice.domain.entities.SupplierRating;
import com.ttdat.purchaseservice.domain.events.supplier.SupplierRatingCreatedEvent;
import com.ttdat.purchaseservice.domain.events.supplier.SupplierRatingUpdatedEvent;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SupplierRatingMapper extends EntityMapper<SupplierRatingDTO, SupplierRating> {
    @Mapping(target = "supplier.supplierId", source = "supplierId")
    SupplierRating toEntity(SupplierRatingCreatedEvent supplierRatingCreatedEvent);

    @Mapping(target = "supplier.supplierId", source = "supplierId")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromEvent(@MappingTarget SupplierRating supplierRating, SupplierRatingUpdatedEvent supplierRatingUpdatedEvent);
}
