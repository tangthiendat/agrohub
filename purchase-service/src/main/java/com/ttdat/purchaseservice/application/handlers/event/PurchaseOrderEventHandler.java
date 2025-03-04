package com.ttdat.purchaseservice.application.handlers.event;

import com.ttdat.core.application.exceptions.ErrorCode;
import com.ttdat.core.application.exceptions.ResourceNotFoundException;
import com.ttdat.purchaseservice.application.mappers.PurchaseOrderMapper;
import com.ttdat.purchaseservice.domain.entities.PurchaseOrder;
import com.ttdat.purchaseservice.domain.events.purchaseorder.PurchaseOrderCreatedEvent;
import com.ttdat.purchaseservice.domain.events.purchaseorder.PurchaseOrderStatusUpdatedEvent;
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

    public PurchaseOrder getPurchaseOrderById(String purchaseOrderId) {
        return purchaseOrderRepository.findById(purchaseOrderId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.PURCHASE_ORDER_NOT_FOUND));
    }

    @EventHandler
    public void on(PurchaseOrderStatusUpdatedEvent purchaseOrderStatusUpdatedEvent) {
        PurchaseOrder purchaseOrder = getPurchaseOrderById(purchaseOrderStatusUpdatedEvent.getPurchaseOrderId());
        purchaseOrder.setStatus(purchaseOrderStatusUpdatedEvent.getStatus());
        purchaseOrderRepository.save(purchaseOrder);
    }

}
