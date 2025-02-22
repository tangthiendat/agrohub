package com.ttdat.inventoryservice.application.handlers.event;

import com.ttdat.core.application.exceptions.DuplicateResourceException;
import com.ttdat.core.application.exceptions.ErrorCode;
import com.ttdat.inventoryservice.application.mappers.WarehouseMapper;
import com.ttdat.inventoryservice.domain.entities.Warehouse;
import com.ttdat.inventoryservice.domain.events.WarehouseCreatedEvent;
import com.ttdat.inventoryservice.domain.repositories.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@ProcessingGroup("warehouse-group")
public class WarehouseEventHandler {
    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper warehouseMapper;

    @Transactional
    @EventHandler
    public void on(WarehouseCreatedEvent warehouseCreatedEvent){
        if(warehouseRepository.existsByWarehouseName(warehouseCreatedEvent.getWarehouseName())){
            throw new DuplicateResourceException(ErrorCode.WAREHOUSE_ALREADY_EXISTS);
        }
        Warehouse warehouse = warehouseMapper.toEntity(warehouseCreatedEvent);
        warehouseRepository.save(warehouse);
    }
}
