package com.ttdat.salesservice.domain.aggregate;

import com.ttdat.core.domain.entities.DiscountType;
import com.ttdat.salesservice.application.commands.exportinvoice.CreateExportInvoiceCommand;
import com.ttdat.salesservice.domain.events.exportinvoice.ExportInvoiceCreatedEvent;
import com.ttdat.salesservice.domain.valueobject.EvtExportInvoiceDetail;
import com.ttdat.salesservice.domain.valueobject.EvtExportInvoiceDetailBatch;
import com.ttdat.salesservice.domain.valueobject.EvtExportInvoiceDetailBatchLocation;
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

@Aggregate(type = "ExportInvoiceAggregate")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@NoArgsConstructor
public class ExportInvoiceAggregate {
    @AggregateIdentifier
    String exportInvoiceId;

    Long warehouseId;

    String customerId;

    String userId;

    LocalDate createDate;

    String note;

    List<EvtExportInvoiceDetail> exportInvoiceDetails;

    BigDecimal totalAmount;

    BigDecimal discountValue;

    DiscountType discountType;

    BigDecimal vatRate;

    BigDecimal finalAmount;

    @CommandHandler
    public ExportInvoiceAggregate(CreateExportInvoiceCommand createExportInvoiceCommand){
        List<EvtExportInvoiceDetail> exportInvoiceDetails = createExportInvoiceCommand.getExportInvoiceDetails() != null ?
                createExportInvoiceCommand.getExportInvoiceDetails().stream()
                        .map(exportInvoiceDetail -> {
                            List<EvtExportInvoiceDetailBatch> detailBatches = exportInvoiceDetail.getDetailBatches() != null ?
                                    exportInvoiceDetail.getDetailBatches().stream()
                                            .map(detailBatch -> {
                                                List<EvtExportInvoiceDetailBatchLocation> detailBatchLocations = detailBatch.getDetailBatchLocations() != null ?
                                                        detailBatch.getDetailBatchLocations().stream()
                                                                .map(detailBatchLocation -> EvtExportInvoiceDetailBatchLocation.builder()
                                                                        .batchLocationId(detailBatchLocation.getBatchLocationId())
                                                                        .quantity(detailBatchLocation.getQuantity())
                                                                        .build())
                                                                .toList() : List.of();
                                                return EvtExportInvoiceDetailBatch.builder()
                                                        .exportInvoiceDetailBatchId(detailBatch.getExportInvoiceDetailBatchId())
                                                        .batchId(detailBatch.getBatchId())
                                                        .quantity(detailBatch.getQuantity())
                                                        .detailBatchLocations(detailBatchLocations)
                                                        .build();
                                            })
                                            .toList() : List.of();
                            return EvtExportInvoiceDetail.builder()
                                    .exportInvoiceDetailId(exportInvoiceDetail.getExportInvoiceDetailId())
                                    .productId(exportInvoiceDetail.getProductId())
                                    .productUnitId(exportInvoiceDetail.getProductUnitId())
                                    .quantity(exportInvoiceDetail.getQuantity())
                                    .unitPrice(exportInvoiceDetail.getUnitPrice())
                                    .detailBatches(detailBatches)
                                    .build();
                        })
                        .toList() : List.of();
        ExportInvoiceCreatedEvent exportInvoiceCreatedEvent = ExportInvoiceCreatedEvent.builder()
                .exportInvoiceId(createExportInvoiceCommand.getExportInvoiceId())
                .warehouseId(createExportInvoiceCommand.getWarehouseId())
                .customerId(createExportInvoiceCommand.getCustomerId())
                .userId(createExportInvoiceCommand.getUserId())
                .createdDate(createExportInvoiceCommand.getCreatedDate())
                .note(createExportInvoiceCommand.getNote())
                .exportInvoiceDetails(exportInvoiceDetails)
                .totalAmount(createExportInvoiceCommand.getTotalAmount())
                .discountValue(createExportInvoiceCommand.getDiscountValue())
                .discountType(createExportInvoiceCommand.getDiscountType())
                .vatRate(createExportInvoiceCommand.getVatRate())
                .finalAmount(createExportInvoiceCommand.getFinalAmount())
                .build();
        AggregateLifecycle.apply(exportInvoiceCreatedEvent);
    }


    @EventSourcingHandler
    public void on(ExportInvoiceCreatedEvent exportInvoiceCreatedEvent){
        this.exportInvoiceId = exportInvoiceCreatedEvent.getExportInvoiceId();
        this.warehouseId = exportInvoiceCreatedEvent.getWarehouseId();
        this.customerId = exportInvoiceCreatedEvent.getCustomerId();
        this.userId = exportInvoiceCreatedEvent.getUserId();
        this.createDate = exportInvoiceCreatedEvent.getCreatedDate();
        this.note = exportInvoiceCreatedEvent.getNote();
        this.exportInvoiceDetails = exportInvoiceCreatedEvent.getExportInvoiceDetails();
        this.totalAmount = exportInvoiceCreatedEvent.getTotalAmount();
        this.discountValue = exportInvoiceCreatedEvent.getDiscountValue();
        this.discountType = exportInvoiceCreatedEvent.getDiscountType();
        this.vatRate = exportInvoiceCreatedEvent.getVatRate();
        this.finalAmount = exportInvoiceCreatedEvent.getFinalAmount();
    }
}
