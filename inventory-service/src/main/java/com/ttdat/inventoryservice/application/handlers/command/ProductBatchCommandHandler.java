package com.ttdat.inventoryservice.application.handlers.command;

import com.ttdat.core.application.commands.batch.CreateProductBatchCommand;
import com.ttdat.inventoryservice.application.commands.batch.UpdateProductBatchCommand;
import com.ttdat.inventoryservice.domain.events.batch.EvtProductBatchLocation;
import com.ttdat.inventoryservice.domain.events.batch.ProductBatchCreatedEvent;
import com.ttdat.inventoryservice.domain.events.batch.ProductBatchUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.GenericEventMessage;
import org.springframework.stereotype.Component;

import java.util.List;

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

    @CommandHandler
    public void handle(UpdateProductBatchCommand updateProductBatchCommand){
        List<EvtProductBatchLocation> evtProductBatchLocations = updateProductBatchCommand.getBatchLocations().stream()
                .map(batchLocationDTO -> EvtProductBatchLocation.builder()
                        .batchLocationId(batchLocationDTO.getBatchLocationId())
                        .locationId(batchLocationDTO.getLocationId())
                        .quantity(batchLocationDTO.getQuantity())
                        .build())
                .toList();
        ProductBatchUpdatedEvent productBatchUpdatedEvent = ProductBatchUpdatedEvent.builder()
                .batchId(updateProductBatchCommand.getBatchId())
                .productId(updateProductBatchCommand.getProductId())
                .manufacturingDate(updateProductBatchCommand.getManufacturingDate())
                .expirationDate(updateProductBatchCommand.getExpirationDate())
                .receivedDate(updateProductBatchCommand.getReceivedDate())
                .quantity(updateProductBatchCommand.getQuantity())
                .batchLocations(evtProductBatchLocations)
                .build();
        eventBus.publish(GenericEventMessage.asEventMessage(productBatchUpdatedEvent));
    }
}
