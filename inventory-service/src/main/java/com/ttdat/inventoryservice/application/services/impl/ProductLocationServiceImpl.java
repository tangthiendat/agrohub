package com.ttdat.inventoryservice.application.services.impl;

import com.ttdat.inventoryservice.api.dto.common.ProductLocationDTO;
import com.ttdat.inventoryservice.application.commands.location.CreateProductLocationCommand;
import com.ttdat.inventoryservice.application.commands.location.UpdateProductLocationCommand;
import com.ttdat.inventoryservice.application.services.ProductLocationService;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductLocationServiceImpl implements ProductLocationService {
    private final CommandGateway commandGateway;

    @Override
    public void createProductLocation(ProductLocationDTO productLocationDTO) {
        CreateProductLocationCommand createProductLocationCommand = CreateProductLocationCommand.builder()
                .locationId(UUID.randomUUID().toString())
                .warehouseId(productLocationDTO.getWarehouseId())
                .rackName(productLocationDTO.getRackName())
                .rackType(productLocationDTO.getRackType())
                .rowNumber(productLocationDTO.getRowNumber())
                .columnNumber(productLocationDTO.getColumnNumber())
                .status(productLocationDTO.getStatus())
                .build();
        commandGateway.sendAndWait(createProductLocationCommand);
    }

    @Override
    public void updateProductLocation(String id, ProductLocationDTO productLocationDTO) {
        UpdateProductLocationCommand updateProductLocationCommand = UpdateProductLocationCommand.builder()
                .locationId(id)
                .warehouseId(productLocationDTO.getWarehouseId())
                .rackName(productLocationDTO.getRackName())
                .rackType(productLocationDTO.getRackType())
                .rowNumber(productLocationDTO.getRowNumber())
                .columnNumber(productLocationDTO.getColumnNumber())
                .status(productLocationDTO.getStatus())
                .build();
        commandGateway.sendAndWait(updateProductLocationCommand);
    }
}
