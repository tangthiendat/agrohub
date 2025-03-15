package com.ttdat.inventoryservice.application.services.impl;

import com.ttdat.inventoryservice.api.dto.common.ProductLocationDTO;
import com.ttdat.inventoryservice.application.commands.location.CreateProductLocationCommand;
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
                .warehouseId(productLocationDTO.getWarehouse().getWarehouseId())
                .rackName(productLocationDTO.getRackName())
                .rackType(productLocationDTO.getRackType())
                .rowNumber(productLocationDTO.getRowNumber())
                .columnNumber(productLocationDTO.getColumnNumber())
                .status(productLocationDTO.getStatus())
                .build();
        commandGateway.send(createProductLocationCommand);
    }
}
