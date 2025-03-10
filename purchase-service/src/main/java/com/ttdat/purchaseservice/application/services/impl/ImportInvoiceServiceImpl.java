package com.ttdat.purchaseservice.application.services.impl;

import com.ttdat.core.application.commands.batch.CreateProductBatchCommand;
import com.ttdat.purchaseservice.api.dto.request.CreateImportInvoiceRequest;
import com.ttdat.purchaseservice.application.commands.importinvoice.CmdImportInvoiceDetail;
import com.ttdat.purchaseservice.application.commands.importinvoice.CreateImportInvoiceCommand;
import com.ttdat.purchaseservice.application.services.ImportInvoiceService;
import com.ttdat.purchaseservice.infrastructure.services.IdGeneratorService;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImportInvoiceServiceImpl implements ImportInvoiceService {
    private final CommandGateway commandGateway;
    private final IdGeneratorService idGeneratorService;

    @Override
    public void createImportInvoice(CreateImportInvoiceRequest request) {
        String importInvoiceId = idGeneratorService.generateImportInvoiceId();
        List<CreateProductBatchCommand> createProductBatchCommandList = new ArrayList<>();
        List<CmdImportInvoiceDetail> cmdImportInvoiceDetails = request.getImportInvoiceDetails().stream()
                .map(importInvoiceDetail -> {
                    String importInvoiceDetailId = UUID.randomUUID().toString();
                    importInvoiceDetail.getBatches().forEach(importInvoiceDetailBatch -> {
                        CreateProductBatchCommand createProductBatchCommand = CreateProductBatchCommand.builder()
                                .batchId(idGeneratorService.generateProductBatchId())
                                .warehouseId(request.getWarehouseId())
                                .productId(importInvoiceDetail.getProductId())
                                .importInvoiceDetailId(importInvoiceDetailId)
                                .manufacturingDate(importInvoiceDetailBatch.getManufacturingDate())
                                .expirationDate(importInvoiceDetailBatch.getExpirationDate())
                                .receivedDate(importInvoiceDetailBatch.getReceivedDate())
                                .quantity(importInvoiceDetailBatch.getQuantity())
                                .build();
                        createProductBatchCommandList.add(createProductBatchCommand);
                    });
                    return CmdImportInvoiceDetail.builder()
                            .importInvoiceDetailId(importInvoiceDetailId)
                            .productId(importInvoiceDetail.getProductId())
                            .productUnitId(importInvoiceDetail.getProductUnitId())
                            .quantity(importInvoiceDetail.getQuantity())
                            .unitPrice(importInvoiceDetail.getUnitPrice())
                            .build();
                })
                .toList();
        CreateImportInvoiceCommand createImportInvoiceCommand = CreateImportInvoiceCommand.builder()
                .importInvoiceId(importInvoiceId)
                .warehouseId(request.getWarehouseId())
                .supplierId(request.getSupplierId())
                .userId(request.getUserId())
                .createdDate(request.getCreatedDate())
                .note(request.getNote())
                .importInvoiceDetails(cmdImportInvoiceDetails)
                .totalAmount(request.getTotalAmount())
                .discountValue(request.getDiscountValue())
                .discountType(request.getDiscountType())
                .vatRate(request.getVatRate())
                .finalAmount(request.getFinalAmount())
                .build();
        commandGateway.sendAndWait(createImportInvoiceCommand);
        createProductBatchCommandList.forEach(commandGateway::sendAndWait);
    }
}
