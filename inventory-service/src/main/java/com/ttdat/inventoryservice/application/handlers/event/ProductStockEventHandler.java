package com.ttdat.inventoryservice.application.handlers.event;

import com.ttdat.core.domain.events.ProductStockAddedEvent;
import com.ttdat.inventoryservice.application.mappers.ProductStockMapper;
import com.ttdat.inventoryservice.domain.entities.ProductStock;
import com.ttdat.inventoryservice.domain.repositories.ProductStockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ProcessingGroup("product-stock-group")
public class ProductStockEventHandler {
    private final ProductStockRepository productStockRepository;
    private final ProductStockMapper productStockMapper;

    @EventHandler
    public void on(ProductStockAddedEvent productStockAddedEvent){
        log.info("Product stock added event received: {}", productStockAddedEvent);
        ProductStock existingProductStock = productStockRepository.findProductStock(productStockAddedEvent.getWarehouseId(), productStockAddedEvent.getProductId());
        if(existingProductStock != null){
            existingProductStock.setQuantity(existingProductStock.getQuantity() + productStockAddedEvent.getQuantity());
            productStockRepository.save(existingProductStock);
        } else {
            ProductStock productStock = productStockMapper.toEntity(productStockAddedEvent);
            productStockRepository.save(productStock);
        }

    }
}
