package com.ttdat.purchaseservice.application.mappers;

import com.ttdat.purchaseservice.domain.entities.PurchaseOrderDetail;
import com.ttdat.purchaseservice.domain.events.purchaseorder.PurchaseOrderDetailCreatedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PurchaseOrderDetailMapper {

    @Mapping(target = "purchaseOrder.purchaseOrderId", source = "purchaseOrderId")
    PurchaseOrderDetail toEntity(PurchaseOrderDetailCreatedEvent purchaseOrderDetailCreatedEvent);

}
