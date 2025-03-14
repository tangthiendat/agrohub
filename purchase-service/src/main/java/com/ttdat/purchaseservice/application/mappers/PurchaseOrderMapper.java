package com.ttdat.purchaseservice.application.mappers;

import com.ttdat.purchaseservice.api.dto.common.PurchaseOrderDTO;
import com.ttdat.purchaseservice.api.dto.response.PurchaseOrderListItem;
import com.ttdat.purchaseservice.domain.entities.PurchaseOrder;
import com.ttdat.purchaseservice.domain.events.purchaseorder.PurchaseOrderCreatedEvent;
import com.ttdat.purchaseservice.domain.events.purchaseorder.PurchaseOrderUpdatedEvent;
import org.mapstruct.*;

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

    @Override
    @Mapping(target = "warehouse.warehouseId", source = "warehouseId")
    @Mapping(target = "user.userId", source = "userId")
    PurchaseOrderDTO toDTO(PurchaseOrder entity);

    @Mapping(target = "supplier.supplierId", source = "supplierId")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromEvent(@MappingTarget PurchaseOrder purchaseOrder, PurchaseOrderUpdatedEvent purchaseOrderUpdatedEvent);

    @AfterMapping
    default void setPurchaseOrderIdForDetails(@MappingTarget PurchaseOrder purchaseOrder, PurchaseOrderCreatedEvent purchaseOrderCreatedEvent) {
        if(purchaseOrder.getPurchaseOrderDetails() != null) {
            purchaseOrder.getPurchaseOrderDetails().forEach(purchaseOrderDetail ->
                    purchaseOrderDetail.setPurchaseOrder(PurchaseOrder.builder().purchaseOrderId(purchaseOrder.getPurchaseOrderId()).build()));
        }
    }

    @AfterMapping
    default void setPurchaseOrderIdForDetails(@MappingTarget PurchaseOrder purchaseOrder, PurchaseOrderUpdatedEvent purchaseOrderUpdatedEvent) {
        if(purchaseOrder.getPurchaseOrderDetails() != null) {
            purchaseOrder.getPurchaseOrderDetails().forEach(purchaseOrderDetail ->
                    purchaseOrderDetail.setPurchaseOrder(PurchaseOrder.builder().purchaseOrderId(purchaseOrder.getPurchaseOrderId()).build()));
        }
    }
}
