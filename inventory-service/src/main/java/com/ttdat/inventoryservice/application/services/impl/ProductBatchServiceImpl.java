package com.ttdat.inventoryservice.application.services.impl;

import com.ttdat.inventoryservice.api.dto.common.ProductBatchDTO;
import com.ttdat.inventoryservice.application.commands.batch.CmdProductBatchLocation;
import com.ttdat.inventoryservice.application.commands.batch.UpdateProductBatchCommand;
import com.ttdat.inventoryservice.application.services.ProductBatchService;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductBatchServiceImpl implements ProductBatchService {
    private final CommandGateway commandGateway;

    @Override
    public void updateProductBatchLocation(String id, ProductBatchDTO productBatchDTO) {
        List<CmdProductBatchLocation> cmdProductBatchLocations = productBatchDTO.getBatchLocations().stream()
                .map(batchLocationDTO -> CmdProductBatchLocation.builder()
                        .batchLocationId(batchLocationDTO.getBatchLocationId() != null ? batchLocationDTO.getBatchLocationId() : UUID.randomUUID().toString())
                        .locationId(batchLocationDTO.getProductLocation().getLocationId())
                        .quantity(batchLocationDTO.getQuantity())
                        .build())
                .toList();
        UpdateProductBatchCommand updateProductBatchCommand = UpdateProductBatchCommand.builder()
                .batchId(id)
                .productId(productBatchDTO.getProduct().getProductId())
                .manufacturingDate(productBatchDTO.getManufacturingDate())
                .expirationDate(productBatchDTO.getExpirationDate())
                .receivedDate(productBatchDTO.getReceivedDate())
                .quantity(productBatchDTO.getQuantity())
                .batchLocations(cmdProductBatchLocations)
                .build();
        commandGateway.sendAndWait(updateProductBatchCommand);
    }
}
