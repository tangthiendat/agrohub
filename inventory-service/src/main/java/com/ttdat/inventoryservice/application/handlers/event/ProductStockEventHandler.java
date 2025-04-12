package com.ttdat.inventoryservice.application.handlers.event;

import com.ttdat.core.application.exceptions.BusinessException;
import com.ttdat.core.application.exceptions.ErrorCode;
import com.ttdat.inventoryservice.domain.events.stock.ProductStockAddedEvent;
import com.ttdat.inventoryservice.application.mappers.ProductStockMapper;
import com.ttdat.inventoryservice.domain.entities.ProductStock;
import com.ttdat.inventoryservice.domain.events.stock.ProductStockReducedEvent;
import com.ttdat.inventoryservice.domain.repositories.ProductStockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
@ProcessingGroup("product-stock-group")
public class ProductStockEventHandler {
    private final ProductStockRepository productStockRepository;
    private final ProductStockMapper productStockMapper;

    @Transactional
    @EventHandler
    public void on(ProductStockAddedEvent productStockAddedEvent){
        ProductStock existingProductStock = productStockRepository.findProductStock(productStockAddedEvent.getWarehouseId(), productStockAddedEvent.getProductId());
        if(existingProductStock != null){
            existingProductStock.setQuantity(existingProductStock.getQuantity() + productStockAddedEvent.getQuantity());
            productStockRepository.save(existingProductStock);
        } else {
            ProductStock productStock = productStockMapper.toEntity(productStockAddedEvent);
            productStockRepository.save(productStock);
        }
    }

    @Transactional
    @EventHandler
    public void on(ProductStockReducedEvent productStockReducedEvent){
        log.info("ProductStockReducedEvent: {}", productStockReducedEvent);
        ProductStock existingProductStock = productStockRepository.findProductStock(productStockReducedEvent.getWarehouseId(), productStockReducedEvent.getProductId());
        if(existingProductStock.getQuantity() < productStockReducedEvent.getQuantity()){
            throw new BusinessException(ErrorCode.PRODUCT_OUT_OF_STOCK);
        } else {
            existingProductStock.setQuantity(existingProductStock.getQuantity() - productStockReducedEvent.getQuantity());
            productStockRepository.save(existingProductStock);
        }
    }
}
