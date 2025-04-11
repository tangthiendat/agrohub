package com.ttdat.purchaseservice.domain.aggregate;

import com.ttdat.purchaseservice.application.commands.importinvoice.CreateImportInvoiceCommand;
import com.ttdat.core.domain.entities.DiscountType;
import com.ttdat.purchaseservice.domain.events.importinvoice.ImportInvoiceCreatedEvent;
import com.ttdat.purchaseservice.domain.valueobject.EvtImportInvoiceDetail;
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

@Aggregate(type = "ImportInvoiceAggregate")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@NoArgsConstructor
public class ImportInvoiceAggregate {
    @AggregateIdentifier
    String importInvoiceId;

    Long warehouseId;

    String supplierId;

    String userId;

    LocalDate createDate;

    String note;

    List<EvtImportInvoiceDetail> importInvoiceDetails;

    BigDecimal totalAmount;

    BigDecimal discountValue;

    DiscountType discountType;

    BigDecimal vatRate;

    BigDecimal finalAmount;

    @CommandHandler
    public ImportInvoiceAggregate(CreateImportInvoiceCommand importInvoiceCommand) {
        List<EvtImportInvoiceDetail> evtImportInvoiceDetails = importInvoiceCommand.getImportInvoiceDetails() != null ?
                importInvoiceCommand.getImportInvoiceDetails().stream()
                .map(importInvoiceDetail -> EvtImportInvoiceDetail.builder()
                        .importInvoiceDetailId(importInvoiceDetail.getImportInvoiceDetailId())
                        .productId(importInvoiceDetail.getProductId())
                        .productUnitId(importInvoiceDetail.getProductUnitId())
                        .quantity(importInvoiceDetail.getQuantity())
                        .unitPrice(importInvoiceDetail.getUnitPrice())
                        .build())
                .toList() : List.of();
        ImportInvoiceCreatedEvent importInvoiceCreatedEvent = ImportInvoiceCreatedEvent.builder()
                .importInvoiceId(importInvoiceCommand.getImportInvoiceId())
                .warehouseId(importInvoiceCommand.getWarehouseId())
                .supplierId(importInvoiceCommand.getSupplierId())
                .userId(importInvoiceCommand.getUserId())
                .createdDate(importInvoiceCommand.getCreatedDate())
                .note(importInvoiceCommand.getNote())
                .importInvoiceDetails(evtImportInvoiceDetails)
                .totalAmount(importInvoiceCommand.getTotalAmount())
                .discountValue(importInvoiceCommand.getDiscountValue())
                .discountType(importInvoiceCommand.getDiscountType())
                .vatRate(importInvoiceCommand.getVatRate())
                .finalAmount(importInvoiceCommand.getFinalAmount())
                .build();
        AggregateLifecycle.apply(importInvoiceCreatedEvent);
    }

    @EventSourcingHandler
    public void on(ImportInvoiceCreatedEvent importInvoiceCreatedEvent) {
        this.importInvoiceId = importInvoiceCreatedEvent.getImportInvoiceId();
        this.warehouseId = importInvoiceCreatedEvent.getWarehouseId();
        this.supplierId = importInvoiceCreatedEvent.getSupplierId();
        this.userId = importInvoiceCreatedEvent.getUserId();
        this.createDate = importInvoiceCreatedEvent.getCreatedDate();
        this.note = importInvoiceCreatedEvent.getNote();
        this.importInvoiceDetails = importInvoiceCreatedEvent.getImportInvoiceDetails();
        this.totalAmount = importInvoiceCreatedEvent.getTotalAmount();
        this.discountValue = importInvoiceCreatedEvent.getDiscountValue();
        this.discountType = importInvoiceCreatedEvent.getDiscountType();
        this.vatRate = importInvoiceCreatedEvent.getVatRate();
        this.finalAmount = importInvoiceCreatedEvent.getFinalAmount();
    }
}
