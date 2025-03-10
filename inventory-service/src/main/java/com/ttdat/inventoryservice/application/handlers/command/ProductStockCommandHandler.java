package com.ttdat.inventoryservice.application.handlers.command;

import com.ttdat.core.application.commands.stock.AddProductStockCommand;
import com.ttdat.core.domain.events.ProductStockAddedEvent;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.GenericEventMessage;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductStockCommandHandler {
    private final EventBus eventBus;

    @CommandHandler
    public void handle(AddProductStockCommand addProductStockCommand) {
        ProductStockAddedEvent productStockAddedEvent = ProductStockAddedEvent.builder()
                .productStockId(addProductStockCommand.getProductStockId())
                .warehouseId(addProductStockCommand.getWarehouseId())
                .productId(addProductStockCommand.getProductId())
                .quantity(addProductStockCommand.getQuantity())
                .build();
        eventBus.publish(GenericEventMessage.asEventMessage(productStockAddedEvent));
    }
}
