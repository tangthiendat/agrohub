package com.ttdat.purchaseservice.application.handlers.event;

import com.ttdat.purchaseservice.application.mappers.PurchaseOrderDetailMapper;
import com.ttdat.purchaseservice.domain.entities.PurchaseOrderDetail;
import com.ttdat.purchaseservice.domain.events.purchaseorder.PurchaseOrderDetailCreatedEvent;
import com.ttdat.purchaseservice.domain.repositories.PurchaseOrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ProcessingGroup("purchase-order-detail-group")
public class PurchaseOrderDetailEventHandler {
    private final PurchaseOrderDetailRepository purchaseOrderDetailRepository;
    private final PurchaseOrderDetailMapper purchaseOrderDetailMapper;

    @EventHandler
    public void on(PurchaseOrderDetailCreatedEvent purchaseOrderDetailCreatedEvent) {
        PurchaseOrderDetail purchaseOrderDetail = purchaseOrderDetailMapper.toEntity(purchaseOrderDetailCreatedEvent);
        purchaseOrderDetailRepository.save(purchaseOrderDetail);
    }
}
