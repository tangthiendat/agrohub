package com.ttdat.productservice.application.handlers.event;

import com.ttdat.core.application.exceptions.ErrorCode;
import com.ttdat.core.application.exceptions.ResourceNotFoundException;
import com.ttdat.productservice.application.mappers.ProductMapper;
import com.ttdat.productservice.domain.entities.Product;
import com.ttdat.productservice.domain.events.product.ProductCreatedEvent;
import com.ttdat.productservice.domain.events.product.ProductUpdatedEvent;
import com.ttdat.productservice.domain.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@ProcessingGroup("product-group")
@RequiredArgsConstructor
public class ProductEventHandler {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Transactional
    @EventHandler
    public void on(ProductCreatedEvent productCreatedEvent) {
        Product product = productMapper.toEntity(productCreatedEvent);
        productRepository.save(product);
    }

    private Product getProductById(String productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    @Transactional
    @EventHandler
    public void on(ProductUpdatedEvent productUpdatedEvent) {
        Product product = getProductById(productUpdatedEvent.getProductId());
        productMapper.updateEntityFromEvent(product, productUpdatedEvent);
        productRepository.save(product);
    }

}
