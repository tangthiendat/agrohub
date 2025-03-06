package com.ttdat.purchaseservice.application.mappers;

import com.ttdat.purchaseservice.api.dto.common.PurchaseOrderDetailDTO;
import com.ttdat.purchaseservice.domain.aggregate.EvtPurchaseOrderDetail;
import com.ttdat.purchaseservice.domain.entities.PurchaseOrder;
import com.ttdat.purchaseservice.domain.entities.PurchaseOrderDetail;
import com.ttdat.purchaseservice.domain.events.purchaseorder.PurchaseOrderDetailCreatedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PurchaseOrderDetailMapper extends EntityMapper<PurchaseOrderDetailDTO, PurchaseOrderDetail> {

    @Override
    @Mapping(target = "product.productId", source = "productId")
    @Mapping(target = "productUnit.productUnitId", source = "productUnitId")
    PurchaseOrderDetailDTO toDTO(PurchaseOrderDetail entity);

    PurchaseOrderDetail toEntity(EvtPurchaseOrderDetail evtPurchaseOrderDetail);

    List<PurchaseOrderDetail> toEntityList(List<EvtPurchaseOrderDetail> purchaseOrderDetails);
}
