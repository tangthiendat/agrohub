package com.ttdat.purchaseservice.application.handlers.event;

import com.ttdat.core.application.exceptions.ErrorCode;
import com.ttdat.core.application.exceptions.ResourceNotFoundException;
import com.ttdat.purchaseservice.application.mappers.PurchaseOrderMapper;
import com.ttdat.purchaseservice.domain.entities.PurchaseOrder;
import com.ttdat.purchaseservice.domain.entities.PurchaseOrderStatus;
import com.ttdat.purchaseservice.domain.events.purchaseorder.PurchaseOrderCancelledEvent;
import com.ttdat.purchaseservice.domain.events.purchaseorder.PurchaseOrderCreatedEvent;
import com.ttdat.purchaseservice.domain.events.purchaseorder.PurchaseOrderStatusUpdatedEvent;
import com.ttdat.purchaseservice.domain.events.purchaseorder.PurchaseOrderUpdatedEvent;
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

    @EventHandler
    public void on(PurchaseOrderUpdatedEvent purchaseOrderUpdatedEvent) {
        PurchaseOrder purchaseOrder = getPurchaseOrderById(purchaseOrderUpdatedEvent.getPurchaseOrderId());
        purchaseOrderMapper.updateEntityFromEvent(purchaseOrder, purchaseOrderUpdatedEvent);
        purchaseOrderRepository.save(purchaseOrder);
    }

    @EventHandler
    public void on(PurchaseOrderCancelledEvent purchaseOrderCancelledEvent) {
        PurchaseOrder purchaseOrder = getPurchaseOrderById(purchaseOrderCancelledEvent.getPurchaseOrderId());
        purchaseOrder.setStatus(PurchaseOrderStatus.CANCELLED);
        purchaseOrder.setCancelReason(purchaseOrderCancelledEvent.getCancelReason());
        purchaseOrderRepository.save(purchaseOrder);
    }

}
