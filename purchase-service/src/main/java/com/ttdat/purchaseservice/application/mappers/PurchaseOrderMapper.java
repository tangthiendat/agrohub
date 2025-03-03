package com.ttdat.purchaseservice.application.mappers;

import com.ttdat.purchaseservice.domain.entities.PurchaseOrder;
import com.ttdat.purchaseservice.domain.events.purchaseorder.PurchaseOrderCreatedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PurchaseOrderMapper {
    @Mapping(target = "supplier.supplierId", source = "supplierId")
    PurchaseOrder toEntity(PurchaseOrderCreatedEvent purchaseOrderCreatedEvent);
}
