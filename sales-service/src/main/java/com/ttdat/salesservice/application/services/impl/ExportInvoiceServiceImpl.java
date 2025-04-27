package com.ttdat.salesservice.application.services.impl;

import com.ttdat.salesservice.api.dto.request.CreateExportInvoiceRequest;
import com.ttdat.salesservice.application.commands.exportinvoice.CmdExportInvoiceDetail;
import com.ttdat.salesservice.application.commands.exportinvoice.CmdExportInvoiceDetailBatch;
import com.ttdat.salesservice.application.commands.exportinvoice.CmdExportInvoiceDetailBatchLocation;
import com.ttdat.salesservice.application.commands.exportinvoice.CreateExportInvoiceCommand;
import com.ttdat.salesservice.application.services.ExportInvoiceService;
import com.ttdat.salesservice.infrastructure.services.IdGeneratorService;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExportInvoiceServiceImpl implements ExportInvoiceService {
    private final CommandGateway commandGateway;
    private final IdGeneratorService idGeneratorService;

    @Override
    public void createExportInvoice(CreateExportInvoiceRequest createExportInvoiceRequest) {
        List<CmdExportInvoiceDetail> exportInvoiceDetails = createExportInvoiceRequest.getExportInvoiceDetails().stream()
                .map(exportInvoiceDetail -> {
                    List<CmdExportInvoiceDetailBatch> exportInvoiceDetailBatches = exportInvoiceDetail.getDetailBatches().stream()
                            .map(exportInvoiceDetailBatch -> {
                                List<CmdExportInvoiceDetailBatchLocation> exportInvoiceDetailBatchLocations = exportInvoiceDetailBatch.getDetailBatchLocations().stream()
                                        .map(detailBatchLocation -> CmdExportInvoiceDetailBatchLocation.builder()
                                                .batchLocationId(detailBatchLocation.getBatchLocationId())
                                                .quantity(detailBatchLocation.getQuantity())
                                                .build())
                                        .toList();
                                return CmdExportInvoiceDetailBatch.builder()
                                        .exportInvoiceDetailBatchId(UUID.randomUUID().toString())
                                        .batchId(exportInvoiceDetailBatch.getBatchId())
                                        .quantity(exportInvoiceDetailBatch.getQuantity())
                                        .detailBatchLocations(exportInvoiceDetailBatchLocations)
                                        .build();
                            })
                            .toList();
                    return CmdExportInvoiceDetail.builder()
                            .exportInvoiceDetailId(UUID.randomUUID().toString())
                            .productId(exportInvoiceDetail.getProductId())
                            .productUnitId(exportInvoiceDetail.getProductUnitId())
                            .quantity(exportInvoiceDetail.getQuantity())
                            .unitPrice(exportInvoiceDetail.getUnitPrice())
                            .detailBatches(exportInvoiceDetailBatches)
                            .build();
                })
                .toList();
        String exportInvoiceId = idGeneratorService.generateExportInvoiceId();
        CreateExportInvoiceCommand createExportInvoiceCommand = CreateExportInvoiceCommand.builder()
                .exportInvoiceId(exportInvoiceId)
                .warehouseId(createExportInvoiceRequest.getWarehouseId())
                .customerId(createExportInvoiceRequest.getCustomerId())
                .userId(createExportInvoiceRequest.getUserId())
                .createdDate(createExportInvoiceRequest.getCreatedDate())
                .note(createExportInvoiceRequest.getNote())
                .exportInvoiceDetails(exportInvoiceDetails)
                .totalAmount(createExportInvoiceRequest.getTotalAmount())
                .discountValue(createExportInvoiceRequest.getDiscountValue())
                .discountType(createExportInvoiceRequest.getDiscountType())
                .vatRate(createExportInvoiceRequest.getVatRate())
                .finalAmount(createExportInvoiceRequest.getFinalAmount())
                .build();
        commandGateway.sendAndWait(createExportInvoiceCommand);
    }
}
