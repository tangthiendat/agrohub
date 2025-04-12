package com.ttdat.inventoryservice.application.handlers.event;

import com.ttdat.core.application.exceptions.BusinessException;
import com.ttdat.core.application.exceptions.ErrorCode;
import com.ttdat.core.application.exceptions.ResourceNotFoundException;
import com.ttdat.inventoryservice.application.commands.location.UpdateProductLocationStatusCommand;
import com.ttdat.inventoryservice.application.mappers.ProductBatchMapper;
import com.ttdat.inventoryservice.domain.entities.LocationStatus;
import com.ttdat.inventoryservice.domain.entities.ProductBatch;
import com.ttdat.inventoryservice.domain.entities.ProductBatchLocation;
import com.ttdat.inventoryservice.domain.events.batch.ProductBatchLocationQuantityReducedEvent;
import com.ttdat.inventoryservice.domain.events.batch.ProductBatchQuantityReducedEvent;
import com.ttdat.inventoryservice.domain.events.batch.ProductBatchCreatedEvent;
import com.ttdat.inventoryservice.domain.events.batch.ProductBatchUpdatedEvent;
import com.ttdat.inventoryservice.domain.repositories.ProductBatchLocationRepository;
import com.ttdat.inventoryservice.domain.repositories.ProductBatchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
@ProcessingGroup("product-batch-group")
public class ProductBatchEventHandler {
    private final ProductBatchRepository productBatchRepository;
    private final ProductBatchLocationRepository productBatchLocationRepository;
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

    @Transactional
    @EventHandler
    public void on(ProductBatchQuantityReducedEvent productBatchQuantityReducedEvent){
        ProductBatch productBatch = getProductBatchById(productBatchQuantityReducedEvent.getBatchId());
        if(productBatch.getQuantity() < productBatchQuantityReducedEvent.getQuantity()){
            throw new BusinessException(ErrorCode.BATCH_OUT_OF_STOCK);
        }
        productBatch.setQuantity(productBatch.getQuantity() - productBatchQuantityReducedEvent.getQuantity());
        productBatchRepository.save(productBatch);
    }

    private ProductBatchLocation getProductBatchLocationById(String batchLocationId) {
        return productBatchLocationRepository.findById(batchLocationId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.PRODUCT_BATCH_LOCATION_NOT_FOUND));
    }

    @Transactional
    @EventHandler
    public void handle(ProductBatchLocationQuantityReducedEvent productBatchLocationQuantityReducedEvent){
        ProductBatchLocation productBatchLocation = getProductBatchLocationById(productBatchLocationQuantityReducedEvent.getBatchLocationId());
        if(productBatchLocation.getQuantity() < productBatchLocationQuantityReducedEvent.getQuantity()){
            throw new ResourceNotFoundException(ErrorCode.LOCATION_OUT_OF_STOCK);
        }
        productBatchLocation.setQuantity(productBatchLocation.getQuantity() - productBatchLocationQuantityReducedEvent.getQuantity());
        productBatchLocationRepository.save(productBatchLocation);

        if(productBatchLocation.getQuantity() == 0){
            UpdateProductLocationStatusCommand updateProductLocationStatusCommand = UpdateProductLocationStatusCommand.builder()
                    .locationId(productBatchLocation.getProductLocation().getLocationId())
                    .status(LocationStatus.AVAILABLE)
                    .build();
            commandGateway.send(updateProductLocationStatusCommand);
        }

    }

}
