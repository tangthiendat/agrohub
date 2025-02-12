package com.ttdat.productservice.application.handlers.event;

import com.ttdat.productservice.application.mappers.ProductMapper;
import com.ttdat.productservice.domain.entities.Category;
import com.ttdat.productservice.domain.entities.Product;
import com.ttdat.productservice.domain.entities.ProductUnit;
import com.ttdat.productservice.domain.events.product.ProductCreatedEvent;
import com.ttdat.productservice.domain.repositories.CategoryRepository;
import com.ttdat.productservice.domain.repositories.ProductRepository;
import com.ttdat.productservice.domain.repositories.ProductUnitPriceRepository;
import com.ttdat.productservice.domain.repositories.ProductUnitRepository;
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
    private final ProductUnitRepository productUnitRepository;
    private final CategoryRepository categoryRepository;
    private final ProductUnitPriceRepository productUnitPriceRepository;
    private final ProductMapper productMapper;

    @Transactional
    @EventHandler
    public void handle(ProductCreatedEvent productCreatedEvent) {
        Product product = productMapper.toEntity(productCreatedEvent);
        productRepository.save(product);
    }

}
