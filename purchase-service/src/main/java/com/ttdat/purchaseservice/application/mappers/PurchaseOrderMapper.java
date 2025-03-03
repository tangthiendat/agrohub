package com.ttdat.purchaseservice.application.mappers;

import com.ttdat.purchaseservice.api.dto.response.PurchaseOrderTableItem;
import com.ttdat.purchaseservice.domain.entities.PurchaseOrder;
import com.ttdat.purchaseservice.domain.events.purchaseorder.PurchaseOrderCreatedEvent;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {SupplierMapper.class})
public interface PurchaseOrderMapper {
    @Mapping(target = "supplier.supplierId", source = "supplierId")
    PurchaseOrder toEntity(PurchaseOrderCreatedEvent purchaseOrderCreatedEvent);

    @Mapping(target = "supplierName", source = "supplier.supplierName")
    PurchaseOrderTableItem toTableItem(PurchaseOrder purchaseOrder);

    List<PurchaseOrderTableItem> toTableItemList(List<PurchaseOrder> purchaseOrders);
}
