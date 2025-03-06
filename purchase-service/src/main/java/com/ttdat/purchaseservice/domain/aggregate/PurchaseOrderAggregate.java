package com.ttdat.purchaseservice.domain.aggregate;

import com.ttdat.purchaseservice.application.commands.purchaseorder.CreatePurchaseOrderCommand;
import com.ttdat.purchaseservice.application.commands.purchaseorder.UpdatePurchaseOrderStatusCommand;
import com.ttdat.purchaseservice.domain.entities.DiscountType;
import com.ttdat.purchaseservice.domain.entities.PurchaseOrderStatus;
import com.ttdat.purchaseservice.domain.events.purchaseorder.PurchaseOrderCreatedEvent;
import com.ttdat.purchaseservice.domain.events.purchaseorder.PurchaseOrderStatusUpdatedEvent;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Aggregate(type = "PurchaseOrderAggregate")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@NoArgsConstructor
public class PurchaseOrderAggregate {
    @AggregateIdentifier
    String purchaseOrderId;

    Long warehouseId;

    String supplierId;

    String userId;

    LocalDate orderDate;

    LocalDate expectedDeliveryDate;

    PurchaseOrderStatus status;

    List<EvtPurchaseOrderDetail> purchaseOrderDetails;

    BigDecimal totalAmount;

    BigDecimal discountValue;

    DiscountType discountType;

    BigDecimal vatRate;

    BigDecimal finalAmount;

    String note;

    @CommandHandler
    public PurchaseOrderAggregate(CreatePurchaseOrderCommand purchaseOrderCommand) {
        List<EvtPurchaseOrderDetail> evtPurchaseOrderDetails = purchaseOrderCommand.getPurchaseOrderDetails() != null ?
                purchaseOrderCommand.getPurchaseOrderDetails().stream()
                        .map(cmdPurchaseOrderDetail -> EvtPurchaseOrderDetail.builder()
                                .purchaseOrderDetailId(cmdPurchaseOrderDetail.getPurchaseOrderDetailId())
                                .productId(cmdPurchaseOrderDetail.getProductId())
                                .productUnitId(cmdPurchaseOrderDetail.getProductUnitId())
                                .quantity(cmdPurchaseOrderDetail.getQuantity())
                                .build())
                        .toList()
                : List.of();
        PurchaseOrderCreatedEvent purchaseOrderCreatedEvent = PurchaseOrderCreatedEvent.builder()
                .purchaseOrderId(purchaseOrderCommand.getPurchaseOrderId())
                .warehouseId(purchaseOrderCommand.getWarehouseId())
                .supplierId(purchaseOrderCommand.getSupplierId())
                .userId(purchaseOrderCommand.getUserId())
                .orderDate(purchaseOrderCommand.getOrderDate())
                .expectedDeliveryDate(purchaseOrderCommand.getExpectedDeliveryDate())
                .status(purchaseOrderCommand.getStatus())
                .totalAmount(purchaseOrderCommand.getTotalAmount())
                .discountValue(purchaseOrderCommand.getDiscountValue())
                .discountType(purchaseOrderCommand.getDiscountType())
                .vatRate(purchaseOrderCommand.getVatRate())
                .purchaseOrderDetails(evtPurchaseOrderDetails)
                .finalAmount(purchaseOrderCommand.getFinalAmount())
                .note(purchaseOrderCommand.getNote())
                .build();
        AggregateLifecycle.apply(purchaseOrderCreatedEvent);
    }

    @CommandHandler
    public void handle(UpdatePurchaseOrderStatusCommand updatePurchaseOrderStatusCommand){
        PurchaseOrderStatusUpdatedEvent purchaseOrderStatusUpdatedEvent = PurchaseOrderStatusUpdatedEvent.builder()
                .purchaseOrderId(updatePurchaseOrderStatusCommand.getPurchaseOrderId())
                .status(updatePurchaseOrderStatusCommand.getStatus())
                .build();
        AggregateLifecycle.apply(purchaseOrderStatusUpdatedEvent);
    }

    @EventSourcingHandler
    public void on(PurchaseOrderCreatedEvent purchaseOrderCreatedEvent) {
        this.purchaseOrderId = purchaseOrderCreatedEvent.getPurchaseOrderId();
        this.warehouseId = purchaseOrderCreatedEvent.getWarehouseId();
        this.supplierId = purchaseOrderCreatedEvent.getSupplierId();
        this.userId = purchaseOrderCreatedEvent.getUserId();
        this.orderDate = purchaseOrderCreatedEvent.getOrderDate();
        this.expectedDeliveryDate = purchaseOrderCreatedEvent.getExpectedDeliveryDate();
        this.status = purchaseOrderCreatedEvent.getStatus();
        this.totalAmount = purchaseOrderCreatedEvent.getTotalAmount();
        this.discountValue = purchaseOrderCreatedEvent.getDiscountValue();
        this.discountType = purchaseOrderCreatedEvent.getDiscountType();
        this.vatRate = purchaseOrderCreatedEvent.getVatRate();
        this.finalAmount = purchaseOrderCreatedEvent.getFinalAmount();
        if (purchaseOrderCreatedEvent.getNote() != null) {
            this.note = purchaseOrderCreatedEvent.getNote();
        }
    }


    @EventSourcingHandler
    public void on(PurchaseOrderStatusUpdatedEvent purchaseOrderStatusUpdatedEvent){
        this.status = purchaseOrderStatusUpdatedEvent.getStatus();
    }
}
