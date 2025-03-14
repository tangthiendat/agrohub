package com.ttdat.inventoryservice.application.handlers.command;

import com.ttdat.core.application.commands.batch.CreateProductBatchCommand;
import com.ttdat.inventoryservice.domain.events.batch.ProductBatchCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.GenericEventMessage;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductBatchCommandHandler {
    private final EventBus eventBus;

    @CommandHandler
    public void handle(CreateProductBatchCommand createProductBatchCommand){
        ProductBatchCreatedEvent productBatchCreatedEvent = ProductBatchCreatedEvent.builder()
                .batchId(createProductBatchCommand.getBatchId())
                .warehouseId(createProductBatchCommand.getWarehouseId())
                .productId(createProductBatchCommand.getProductId())
                .importInvoiceDetailId(createProductBatchCommand.getImportInvoiceDetailId())
                .manufacturingDate(createProductBatchCommand.getManufacturingDate())
                .expirationDate(createProductBatchCommand.getExpirationDate())
                .receivedDate(createProductBatchCommand.getReceivedDate())
                .quantity(createProductBatchCommand.getQuantity())
                .build();
        eventBus.publish(GenericEventMessage.asEventMessage(productBatchCreatedEvent));
    }
}
