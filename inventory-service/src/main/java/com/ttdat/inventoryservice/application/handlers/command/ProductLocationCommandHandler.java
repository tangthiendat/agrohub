package com.ttdat.inventoryservice.application.handlers.command;

import com.ttdat.inventoryservice.application.commands.location.CreateProductLocationCommand;
import com.ttdat.inventoryservice.application.commands.location.UpdateProductLocationCommand;
import com.ttdat.inventoryservice.application.commands.location.UpdateProductLocationStatusCommand;
import com.ttdat.inventoryservice.domain.events.location.ProductLocationCreatedEvent;
import com.ttdat.inventoryservice.domain.events.location.ProductLocationStatusUpdatedEvent;
import com.ttdat.inventoryservice.domain.events.location.ProductLocationUpdatedEvent;
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

    @CommandHandler
    public void handle(UpdateProductLocationCommand updateProductLocationCommand){
        ProductLocationUpdatedEvent productLocationUpdatedEvent = ProductLocationUpdatedEvent.builder()
                .locationId(updateProductLocationCommand.getLocationId())
                .warehouseId(updateProductLocationCommand.getWarehouseId())
                .rackName(updateProductLocationCommand.getRackName())
                .rackType(updateProductLocationCommand.getRackType())
                .rowNumber(updateProductLocationCommand.getRowNumber())
                .columnNumber(updateProductLocationCommand.getColumnNumber())
                .status(updateProductLocationCommand.getStatus())
                .reason(updateProductLocationCommand.getReason())
                .build();
        eventBus.publish(GenericEventMessage.asEventMessage(productLocationUpdatedEvent));
    }

    @CommandHandler
    public void handle(UpdateProductLocationStatusCommand updateProductLocationStatusCommand){
        ProductLocationStatusUpdatedEvent productLocationStatusUpdatedEvent = ProductLocationStatusUpdatedEvent.builder()
                .locationId(updateProductLocationStatusCommand.getLocationId())
                .status(updateProductLocationStatusCommand.getStatus())
                .build();
        eventBus.publish(GenericEventMessage.asEventMessage(productLocationStatusUpdatedEvent));
    }
}
