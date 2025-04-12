package com.ttdat.inventoryservice.application.handlers.command;

import com.ttdat.core.application.commands.stock.AddProductStockCommand;
import com.ttdat.inventoryservice.domain.events.stock.ProductStockAddedEvent;
import com.ttdat.inventoryservice.domain.events.stock.ProductStockReducedEvent;
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

    @CommandHandler
    public void handle(com.ttdat.core.application.commands.stock.ReduceProductStockCommand reduceProductStockCommand) {
        ProductStockReducedEvent productStockReducedEvent = ProductStockReducedEvent.builder()
                .warehouseId(reduceProductStockCommand.getWarehouseId())
                .productId(reduceProductStockCommand.getProductId())
                .quantity(reduceProductStockCommand.getQuantity())
                .build();
        eventBus.publish(GenericEventMessage.asEventMessage(productStockReducedEvent));
    }

}
