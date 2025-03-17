package com.ttdat.inventoryservice.application.handlers.event;

import com.ttdat.inventoryservice.application.mappers.ProductBatchMapper;
import com.ttdat.inventoryservice.domain.entities.ProductBatch;
import com.ttdat.inventoryservice.domain.events.batch.ProductBatchCreatedEvent;
import com.ttdat.inventoryservice.domain.repositories.ProductBatchRepository;
import lombok.RequiredArgsConstructor;
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

    @Transactional
    @EventHandler
    public void on(ProductBatchCreatedEvent productBatchCreatedEvent){
        ProductBatch productBatch = productBatchMapper.toEntity(productBatchCreatedEvent);
        productBatchRepository.save(productBatch);
    }
}
