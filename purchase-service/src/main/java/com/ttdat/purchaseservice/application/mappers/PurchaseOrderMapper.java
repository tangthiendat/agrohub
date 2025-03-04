package com.ttdat.purchaseservice.application.mappers;

import com.ttdat.purchaseservice.api.dto.common.PurchaseOrderDTO;
import com.ttdat.purchaseservice.api.dto.response.PurchaseOrderListItem;
import com.ttdat.purchaseservice.domain.entities.PurchaseOrder;
import com.ttdat.purchaseservice.domain.events.purchaseorder.PurchaseOrderCreatedEvent;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {SupplierMapper.class, PurchaseOrderDetailMapper.class})
public interface PurchaseOrderMapper extends EntityMapper<PurchaseOrderDTO, PurchaseOrder> {
    @Mapping(target = "supplier.supplierId", source = "supplierId")
    PurchaseOrder toEntity(PurchaseOrderCreatedEvent purchaseOrderCreatedEvent);

    @Mapping(target = "supplierName", source = "supplier.supplierName")
    PurchaseOrderListItem toListItem(PurchaseOrder purchaseOrder);

    List<PurchaseOrderListItem> toTableItemList(List<PurchaseOrder> purchaseOrders);
}
