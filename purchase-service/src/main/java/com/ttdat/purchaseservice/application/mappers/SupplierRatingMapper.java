package com.ttdat.purchaseservice.application.mappers;

import com.ttdat.purchaseservice.domain.entities.SupplierRating;
import com.ttdat.purchaseservice.domain.events.supplier.SupplierRatingCreatedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SupplierRatingMapper {
    @Mapping(target = "supplier.supplierId", source = "supplierId")
    SupplierRating toEntity(SupplierRatingCreatedEvent supplierRatingCreatedEvent);
}
