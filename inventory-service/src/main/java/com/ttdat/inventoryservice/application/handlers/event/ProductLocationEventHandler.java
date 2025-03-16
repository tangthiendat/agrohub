package com.ttdat.inventoryservice.application.handlers.event;

import com.ttdat.core.application.exceptions.DuplicateResourceException;
import com.ttdat.core.application.exceptions.ErrorCode;
import com.ttdat.core.application.exceptions.ResourceNotFoundException;
import com.ttdat.inventoryservice.application.mappers.ProductLocationMapper;
import com.ttdat.inventoryservice.domain.entities.ProductLocation;
import com.ttdat.inventoryservice.domain.events.location.ProductLocationCreatedEvent;
import com.ttdat.inventoryservice.domain.events.location.ProductLocationUpdatedEvent;
import com.ttdat.inventoryservice.domain.repositories.ProductLocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
@ProcessingGroup("product-location-group")
public class ProductLocationEventHandler {
    private final ProductLocationRepository productLocationRepository;
    private final ProductLocationMapper productLocationMapper;

    @Transactional
    @EventHandler
    public void on(ProductLocationCreatedEvent productLocationCreatedEvent) {
        if (productLocationRepository.exists(productLocationCreatedEvent.getWarehouseId(),
                productLocationCreatedEvent.getRackName(),
                productLocationCreatedEvent.getRackType(),
                productLocationCreatedEvent.getRowNumber(),
                productLocationCreatedEvent.getColumnNumber())) {
            throw new DuplicateResourceException(ErrorCode.PRODUCT_LOCATION_ALREADY_EXISTS);
        }
        ProductLocation productLocation = productLocationMapper.toEntity(productLocationCreatedEvent);
        productLocationRepository.save(productLocation);
    }

    @Transactional
    @EventHandler
    public void on(ProductLocationUpdatedEvent productLocationUpdatedEvent) {
        ProductLocation productLocation = productLocationRepository.findById(productLocationUpdatedEvent.getLocationId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.PRODUCT_LOCATION_NOT_FOUND));
        productLocationMapper.updateEntityFromEvent(productLocation, productLocationUpdatedEvent);
        productLocationRepository.save(productLocation);
    }
}
