package com.ttdat.purchaseservice.application.handlers.event;

import com.ttdat.purchaseservice.application.mappers.PurchaseOrderMapper;
import com.ttdat.purchaseservice.domain.entities.PurchaseOrder;
import com.ttdat.purchaseservice.domain.events.purchaseorder.PurchaseOrderCreatedEvent;
import com.ttdat.purchaseservice.domain.repositories.PurchaseOrderRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ProcessingGroup("purchase-order-group")
public class PurchaseOrderEventHandler {
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderMapper purchaseOrderMapper;

    @EventHandler
    public void on(PurchaseOrderCreatedEvent purchaseOrderCreatedEvent) {
        PurchaseOrder purchaseOrder = purchaseOrderMapper.toEntity(purchaseOrderCreatedEvent);
        purchaseOrderRepository.save(purchaseOrder);
    }
}
