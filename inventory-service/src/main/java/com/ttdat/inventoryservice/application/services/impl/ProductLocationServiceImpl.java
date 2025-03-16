package com.ttdat.inventoryservice.application.services.impl;

import com.ttdat.inventoryservice.api.dto.request.CreateProductLocationRequest;
import com.ttdat.inventoryservice.api.dto.request.UpdateProductLocationRequest;
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
    public void createProductLocation(CreateProductLocationRequest createProductLocationRequest) {
        CreateProductLocationCommand createProductLocationCommand = CreateProductLocationCommand.builder()
                .locationId(UUID.randomUUID().toString())
                .warehouseId(createProductLocationRequest.getWarehouseId())
                .rackName(createProductLocationRequest.getRackName())
                .rackType(createProductLocationRequest.getRackType())
                .rowNumber(createProductLocationRequest.getRowNumber())
                .columnNumber(createProductLocationRequest.getColumnNumber())
                .build();
        commandGateway.send(createProductLocationCommand);
    }

    @Override
    public void updateProductLocation(String id, UpdateProductLocationRequest updateProductLocationRequest) {
        UpdateProductLocationCommand updateProductLocationCommand = UpdateProductLocationCommand.builder()
                .locationId(id)
                .warehouseId(updateProductLocationRequest.getWarehouseId())
                .rackName(updateProductLocationRequest.getRackName())
                .rackType(updateProductLocationRequest.getRackType())
                .rowNumber(updateProductLocationRequest.getRowNumber())
                .columnNumber(updateProductLocationRequest.getColumnNumber())
                .build();
        commandGateway.send(updateProductLocationCommand);
    }
}
