package com.ttdat.inventoryservice.application.handlers.event;

import com.ttdat.core.application.exceptions.ErrorCode;
import com.ttdat.core.application.exceptions.ResourceNotFoundException;
import com.ttdat.inventoryservice.application.commands.location.UpdateProductLocationStatusCommand;
import com.ttdat.inventoryservice.application.mappers.ProductBatchMapper;
import com.ttdat.inventoryservice.domain.entities.LocationStatus;
import com.ttdat.inventoryservice.domain.entities.ProductBatch;
import com.ttdat.inventoryservice.domain.events.batch.ProductBatchCreatedEvent;
import com.ttdat.inventoryservice.domain.events.batch.ProductBatchUpdatedEvent;
import com.ttdat.inventoryservice.domain.repositories.ProductBatchRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@ProcessingGroup("product-batch-group")
public class ProductBatchEventHandler {
    private final ProductBatchRepository productBatchRepository;
    private final ProductBatchMapper productBatchMapper;
    private final CommandGateway commandGateway;

    @Transactional
    @EventHandler
    public void on(ProductBatchCreatedEvent productBatchCreatedEvent){
        ProductBatch productBatch = productBatchMapper.toEntity(productBatchCreatedEvent);
        productBatchRepository.save(productBatch);
    }

    private ProductBatch getProductBatchById(String batchId) {
        return productBatchRepository.findById(batchId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.PRODUCT_BATCH_NOT_FOUND));
    }

    @Transactional
    @EventHandler
    public void on(ProductBatchUpdatedEvent productBatchUpdatedEvent){
        ProductBatch productBatch = getProductBatchById(productBatchUpdatedEvent.getBatchId());
        productBatchMapper.updateEntityFromEvent(productBatch, productBatchUpdatedEvent);
        productBatchRepository.save(productBatch);
        productBatchUpdatedEvent.getBatchLocations().forEach(batchLocation -> {
            UpdateProductLocationStatusCommand updateProductLocationStatusCommand = UpdateProductLocationStatusCommand.builder()
                    .locationId(batchLocation.getLocationId())
                    .status(LocationStatus.OCCUPIED)
                    .build();
            commandGateway.send(updateProductLocationStatusCommand);
        });
    }
}
