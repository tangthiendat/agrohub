package com.ttdat.purchaseservice.domain.aggregate;

import com.ttdat.purchaseservice.application.commands.purchaseorder.CancelPurchaseOrderCommand;
import com.ttdat.purchaseservice.application.commands.purchaseorder.CreatePurchaseOrderCommand;
import com.ttdat.purchaseservice.application.commands.purchaseorder.UpdatePurchaseOrderCommand;
import com.ttdat.purchaseservice.application.commands.purchaseorder.UpdatePurchaseOrderStatusCommand;
import com.ttdat.purchaseservice.domain.entities.DiscountType;
import com.ttdat.purchaseservice.domain.entities.PurchaseOrderStatus;
import com.ttdat.purchaseservice.domain.events.purchaseorder.PurchaseOrderCancelledEvent;
import com.ttdat.purchaseservice.domain.events.purchaseorder.PurchaseOrderCreatedEvent;
import com.ttdat.purchaseservice.domain.events.purchaseorder.PurchaseOrderStatusUpdatedEvent;
import com.ttdat.purchaseservice.domain.events.purchaseorder.PurchaseOrderUpdatedEvent;
import com.ttdat.purchaseservice.domain.valueobject.EvtPurchaseOrderDetail;
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

    String note;

    String cancelReason;

    List<EvtPurchaseOrderDetail> purchaseOrderDetails;

    BigDecimal totalAmount;

    BigDecimal discountValue;

    DiscountType discountType;

    BigDecimal vatRate;

    BigDecimal finalAmount;


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

    @CommandHandler
    public void handle(UpdatePurchaseOrderCommand updatePurchaseOrderCommand){
        List<EvtPurchaseOrderDetail> evtPurchaseOrderDetails = updatePurchaseOrderCommand.getPurchaseOrderDetails() != null ?
                updatePurchaseOrderCommand.getPurchaseOrderDetails().stream()
                        .map(cmdPurchaseOrderDetail -> EvtPurchaseOrderDetail.builder()
                                .purchaseOrderDetailId(cmdPurchaseOrderDetail.getPurchaseOrderDetailId())
                                .productId(cmdPurchaseOrderDetail.getProductId())
                                .productUnitId(cmdPurchaseOrderDetail.getProductUnitId())
                                .quantity(cmdPurchaseOrderDetail.getQuantity())
                                .unitPrice(cmdPurchaseOrderDetail.getUnitPrice())
                                .build())
                        .toList()
                : List.of();
        PurchaseOrderUpdatedEvent purchaseOrderUpdatedEvent = PurchaseOrderUpdatedEvent.builder()
                .purchaseOrderId(updatePurchaseOrderCommand.getPurchaseOrderId())
                .warehouseId(updatePurchaseOrderCommand.getWarehouseId())
                .supplierId(updatePurchaseOrderCommand.getSupplierId())
                .userId(updatePurchaseOrderCommand.getUserId())
                .orderDate(updatePurchaseOrderCommand.getOrderDate())
                .expectedDeliveryDate(updatePurchaseOrderCommand.getExpectedDeliveryDate())
                .status(updatePurchaseOrderCommand.getStatus())
                .purchaseOrderDetails(evtPurchaseOrderDetails)
                .totalAmount(updatePurchaseOrderCommand.getTotalAmount())
                .discountValue(updatePurchaseOrderCommand.getDiscountValue())
                .discountType(updatePurchaseOrderCommand.getDiscountType())
                .vatRate(updatePurchaseOrderCommand.getVatRate())
                .finalAmount(updatePurchaseOrderCommand.getFinalAmount())
                .note(updatePurchaseOrderCommand.getNote())
                .build();
        AggregateLifecycle.apply(purchaseOrderUpdatedEvent);
    }

    @CommandHandler
    public void handle(CancelPurchaseOrderCommand cancelPurchaseOrderCommand){
        PurchaseOrderCancelledEvent purchaseOrderCancelledEvent = PurchaseOrderCancelledEvent.builder()
                .purchaseOrderId(cancelPurchaseOrderCommand.getPurchaseOrderId())
                .cancelReason(cancelPurchaseOrderCommand.getCancelReason())
                .build();
        AggregateLifecycle.apply(purchaseOrderCancelledEvent);
    }

    @EventSourcingHandler
    public void on(PurchaseOrderCreatedEvent purchaseOrderCreatedEvent) {
        this.purchaseOrderId = purchaseOrderCreatedEvent.getPurchaseOrderId();
        this.warehouseId = purchaseOrderCreatedEvent.getWarehouseId();
        this.supplierId = purchaseOrderCreatedEvent.getSupplierId();
        this.userId = purchaseOrderCreatedEvent.getUserId();
        this.orderDate = purchaseOrderCreatedEvent.getOrderDate();
        this.expectedDeliveryDate = purchaseOrderCreatedEvent.getExpectedDeliveryDate();
        this.purchaseOrderDetails = purchaseOrderCreatedEvent.getPurchaseOrderDetails();
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

    @EventSourcingHandler
    public void on(PurchaseOrderUpdatedEvent purchaseOrderUpdatedEvent){
        this.warehouseId = purchaseOrderUpdatedEvent.getWarehouseId();
        this.supplierId = purchaseOrderUpdatedEvent.getSupplierId();
        this.userId = purchaseOrderUpdatedEvent.getUserId();
        this.orderDate = purchaseOrderUpdatedEvent.getOrderDate();
        this.expectedDeliveryDate = purchaseOrderUpdatedEvent.getExpectedDeliveryDate();
        this.status = purchaseOrderUpdatedEvent.getStatus();
        this.purchaseOrderDetails = purchaseOrderUpdatedEvent.getPurchaseOrderDetails();
        this.totalAmount = purchaseOrderUpdatedEvent.getTotalAmount();
        this.discountValue = purchaseOrderUpdatedEvent.getDiscountValue();
        this.discountType = purchaseOrderUpdatedEvent.getDiscountType();
        this.vatRate = purchaseOrderUpdatedEvent.getVatRate();
        this.finalAmount = purchaseOrderUpdatedEvent.getFinalAmount();
        if (purchaseOrderUpdatedEvent.getNote() != null) {
            this.note = purchaseOrderUpdatedEvent.getNote();
        }
    }

    @EventSourcingHandler
    public void on(PurchaseOrderCancelledEvent purchaseOrderCancelledEvent){
        this.cancelReason = purchaseOrderCancelledEvent.getCancelReason();
        this.status = PurchaseOrderStatus.CANCELLED;
    }
}
