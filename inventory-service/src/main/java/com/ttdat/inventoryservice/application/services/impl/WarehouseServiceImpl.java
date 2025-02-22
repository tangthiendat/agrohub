package com.ttdat.inventoryservice.application.services.impl;

import com.ttdat.inventoryservice.api.dto.common.WarehouseDTO;
import com.ttdat.inventoryservice.application.commands.warehouse.CreateWarehouseCommand;
import com.ttdat.inventoryservice.application.services.WarehouseService;
import com.ttdat.inventoryservice.infrastructure.services.IdGeneratorService;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    private final IdGeneratorService idGeneratorService;
    private final CommandGateway commandGateway;

    @Override
    public void createWarehouse(WarehouseDTO warehouseDTO) {
        CreateWarehouseCommand createWarehouseCommand = CreateWarehouseCommand.builder()
                .warehouseId(idGeneratorService.generateWarehouseId())
                .warehouseName(warehouseDTO.getWarehouseName())
                .address(warehouseDTO.getAddress())
                .build();
        commandGateway.sendAndWait(createWarehouseCommand);
    }
}
