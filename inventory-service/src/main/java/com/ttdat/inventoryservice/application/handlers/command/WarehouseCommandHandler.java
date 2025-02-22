package com.ttdat.inventoryservice.application.handlers.command;

import com.ttdat.inventoryservice.application.commands.warehouse.CreateWarehouseCommand;
import com.ttdat.inventoryservice.domain.events.WarehouseCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.GenericEventMessage;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WarehouseCommandHandler {
    private final EventBus eventBus;

    @CommandHandler
    public void handle(CreateWarehouseCommand createWarehouseCommand){
        WarehouseCreatedEvent warehouseCreatedEvent = WarehouseCreatedEvent.builder()
                .warehouseId(createWarehouseCommand.getWarehouseId())
                .warehouseName(createWarehouseCommand.getWarehouseName())
                .address(createWarehouseCommand.getAddress())
                .build();
        eventBus.publish(GenericEventMessage.asEventMessage(warehouseCreatedEvent));
    }
}
