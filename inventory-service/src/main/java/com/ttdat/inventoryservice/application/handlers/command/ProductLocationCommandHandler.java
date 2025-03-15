package com.ttdat.inventoryservice.application.handlers.command;

import com.ttdat.inventoryservice.application.commands.location.CreateProductLocationCommand;
import com.ttdat.inventoryservice.domain.events.location.ProductLocationCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.GenericEventMessage;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductLocationCommandHandler {
    private final EventBus eventBus;

    @CommandHandler
    public void handle(CreateProductLocationCommand createProductLocationCommand){
        ProductLocationCreatedEvent productLocationCreatedEvent = ProductLocationCreatedEvent.builder()
                .locationId(createProductLocationCommand.getLocationId())
                .warehouseId(createProductLocationCommand.getWarehouseId())
                .rackName(createProductLocationCommand.getRackName())
                .rackType(createProductLocationCommand.getRackType())
                .rowNumber(createProductLocationCommand.getRowNumber())
                .columnNumber(createProductLocationCommand.getColumnNumber())
                .status(createProductLocationCommand.getStatus())
                .build();
        eventBus.publish(GenericEventMessage.asEventMessage(productLocationCreatedEvent));
    }
}
