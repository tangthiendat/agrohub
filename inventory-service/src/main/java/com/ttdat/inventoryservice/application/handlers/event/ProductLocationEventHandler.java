package com.ttdat.inventoryservice.application.handlers.event;

import com.ttdat.inventoryservice.application.mappers.ProductLocationMapper;
import com.ttdat.inventoryservice.domain.entities.ProductLocation;
import com.ttdat.inventoryservice.domain.events.location.ProductLocationCreatedEvent;
import com.ttdat.inventoryservice.domain.repositories.ProductLocationRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ProcessingGroup("product-location-group")
public class ProductLocationEventHandler {
    private final ProductLocationRepository productLocationRepository;
    private final ProductLocationMapper productLocationMapper;

    @EventHandler
    public void on(ProductLocationCreatedEvent productLocationCreatedEvent){
        ProductLocation productLocation = productLocationMapper.toEntity(productLocationCreatedEvent);
        productLocationRepository.save(productLocation);
    }
}
