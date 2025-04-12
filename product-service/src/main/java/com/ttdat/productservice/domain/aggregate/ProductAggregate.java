package com.ttdat.productservice.domain.aggregate;

import com.ttdat.core.application.commands.product.ReduceProductTotalQuantityCommand;
import com.ttdat.core.application.commands.product.UpdateProductTotalQuantityCommand;
import com.ttdat.core.application.exceptions.BusinessException;
import com.ttdat.core.application.exceptions.ErrorCode;
import com.ttdat.productservice.application.commands.product.CreateProductCommand;
import com.ttdat.productservice.application.commands.product.UpdateProductCommand;
import com.ttdat.productservice.domain.entities.PhysicalState;
import com.ttdat.productservice.domain.events.product.ProductCreatedEvent;
import com.ttdat.productservice.domain.events.product.ProductTotalQuantityReducedEvent;
import com.ttdat.productservice.domain.events.product.ProductTotalQuantityUpdatedEvent;
import com.ttdat.productservice.domain.events.product.ProductUpdatedEvent;
import com.ttdat.productservice.domain.valueobject.EvtProductUnit;
import com.ttdat.productservice.domain.valueobject.EvtProductUnitPrice;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.List;

@Slf4j
@Aggregate(type = "ProductAggregate", snapshotTriggerDefinition = "productSnapshotTriggerDefinition")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ProductAggregate {
    @AggregateIdentifier
    String productId;

    String productName;

    Double totalQuantity;

    String description;

    String imageUrl;

    Long categoryId;

    Integer defaultExpDays;

    String storageInstructions;

    List<EvtProductUnit> productUnits;

    PhysicalState physicalState;

    String packaging;

    String safetyInstructions;

    String hazardClassification;

    String ppeRequired;

    @CommandHandler
    public ProductAggregate(CreateProductCommand createProductCommand) {
        List<EvtProductUnit> evtProductUnits = createProductCommand.getProductUnits() != null ?
                createProductCommand.getProductUnits().stream()
                        .map(productUnit -> {
                            List<EvtProductUnitPrice> evtProductUnitPrices = productUnit.getProductUnitPrices() != null ?
                                    productUnit.getProductUnitPrices().stream()
                                            .map(productUnitPrice -> EvtProductUnitPrice.builder()
                                                    .productUnitPriceId(productUnitPrice.getProductUnitPriceId())
                                                    .price(productUnitPrice.getPrice())
                                                    .validFrom(productUnitPrice.getValidFrom())
                                                    .validTo(productUnitPrice.getValidTo())
                                                    .build())
                                            .toList()
                                    : List.of();
                            return EvtProductUnit.builder()
                                    .productUnitId(productUnit.getProductUnitId())
                                    .unitId(productUnit.getUnitId())
                                    .productUnitPrices(evtProductUnitPrices)
                                    .conversionFactor(productUnit.getConversionFactor())
                                    .isDefault(productUnit.isDefault())
                                    .build();
                        }).toList()
                : List.of();
        ProductCreatedEvent productCreatedEvent = ProductCreatedEvent.builder()
                .productId(createProductCommand.getProductId())
                .productName(createProductCommand.getProductName())
                .description(createProductCommand.getDescription())
                .totalQuantity(createProductCommand.getTotalQuantity())
                .imageUrl(createProductCommand.getImageUrl())
                .categoryId(createProductCommand.getCategoryId())
                .defaultExpDays(createProductCommand.getDefaultExpDays())
                .storageInstructions(createProductCommand.getStorageInstructions())
                .productUnits(evtProductUnits)
                .physicalState(createProductCommand.getPhysicalState())
                .packaging(createProductCommand.getPackaging())
                .safetyInstructions(createProductCommand.getSafetyInstructions())
                .hazardClassification(createProductCommand.getHazardClassification())
                .ppeRequired(createProductCommand.getPpeRequired())
                .build();
        AggregateLifecycle.apply(productCreatedEvent);
    }

    @CommandHandler
    public void handle(UpdateProductCommand updateProductCommand) {
        List<EvtProductUnit> evtProductUnits = updateProductCommand.getProductUnits() != null ?
                updateProductCommand.getProductUnits().stream()
                        .map(productUnit -> {
                            List<EvtProductUnitPrice> evtProductUnitPrices = productUnit.getProductUnitPrices() != null ?
                                    productUnit.getProductUnitPrices().stream()
                                            .map(productUnitPrice -> EvtProductUnitPrice.builder()
                                                    .productUnitPriceId(productUnitPrice.getProductUnitPriceId())
                                                    .price(productUnitPrice.getPrice())
                                                    .validFrom(productUnitPrice.getValidFrom())
                                                    .validTo(productUnitPrice.getValidTo())
                                                    .build())
                                            .toList()
                                    : List.of();
                            return EvtProductUnit.builder()
                                    .productUnitId(productUnit.getProductUnitId())
                                    .unitId(productUnit.getUnitId())
                                    .productUnitPrices(evtProductUnitPrices)
                                    .conversionFactor(productUnit.getConversionFactor())
                                    .isDefault(productUnit.isDefault())
                                    .build();
                        }).toList()
                : List.of();
        ProductUpdatedEvent productUpdatedEvent = ProductUpdatedEvent.builder()
                .productId(updateProductCommand.getProductId())
                .productName(updateProductCommand.getProductName())
                .description(updateProductCommand.getDescription())
                .totalQuantity(updateProductCommand.getTotalQuantity())
                .imageUrl(updateProductCommand.getImageUrl())
                .categoryId(updateProductCommand.getCategoryId())
                .defaultExpDays(updateProductCommand.getDefaultExpDays())
                .storageInstructions(updateProductCommand.getStorageInstructions())
                .productUnits(evtProductUnits)
                .physicalState(updateProductCommand.getPhysicalState())
                .packaging(updateProductCommand.getPackaging())
                .safetyInstructions(updateProductCommand.getSafetyInstructions())
                .hazardClassification(updateProductCommand.getHazardClassification())
                .ppeRequired(updateProductCommand.getPpeRequired())
                .build();
        AggregateLifecycle.apply(productUpdatedEvent);
    }

    @CommandHandler
    public void handle(UpdateProductTotalQuantityCommand updateProductTotalQuantityCommand) {
        ProductTotalQuantityUpdatedEvent productTotalQuantityUpdatedEvent = ProductTotalQuantityUpdatedEvent.builder()
                .productId(updateProductTotalQuantityCommand.getProductId())
                .quantity(updateProductTotalQuantityCommand.getQuantity())
                .build();
        AggregateLifecycle.apply(productTotalQuantityUpdatedEvent);
    }

    @CommandHandler
    public void handle(ReduceProductTotalQuantityCommand reduceProductTotalQuantityCommand) {
        ProductTotalQuantityReducedEvent productTotalQuantityReducedEvent = ProductTotalQuantityReducedEvent.builder()
                .productId(reduceProductTotalQuantityCommand.getProductId())
                .quantity(reduceProductTotalQuantityCommand.getQuantity())
                .build();
        AggregateLifecycle.apply(productTotalQuantityReducedEvent);
    }

    @EventSourcingHandler
    public void on(ProductCreatedEvent productCreatedEvent) {
        this.productId = productCreatedEvent.getProductId();
        this.productName = productCreatedEvent.getProductName();
        this.description = productCreatedEvent.getDescription();
        this.totalQuantity = productCreatedEvent.getTotalQuantity();
        this.imageUrl = productCreatedEvent.getImageUrl();
        this.categoryId = productCreatedEvent.getCategoryId();
        this.defaultExpDays = productCreatedEvent.getDefaultExpDays();
        this.storageInstructions = productCreatedEvent.getStorageInstructions();
        this.productUnits = productCreatedEvent.getProductUnits();
        this.physicalState = productCreatedEvent.getPhysicalState();
        this.packaging = productCreatedEvent.getPackaging();
        this.safetyInstructions = productCreatedEvent.getSafetyInstructions();
        this.hazardClassification = productCreatedEvent.getHazardClassification();
        this.ppeRequired = productCreatedEvent.getPpeRequired();
    }

    @EventSourcingHandler
    public void on(ProductUpdatedEvent productUpdatedEvent) {
        this.productName = productUpdatedEvent.getProductName();
        this.description = productUpdatedEvent.getDescription();
        this.totalQuantity = productUpdatedEvent.getTotalQuantity();
        this.imageUrl = productUpdatedEvent.getImageUrl();
        this.categoryId = productUpdatedEvent.getCategoryId();
        this.defaultExpDays = productUpdatedEvent.getDefaultExpDays();
        this.storageInstructions = productUpdatedEvent.getStorageInstructions();
        this.productUnits = productUpdatedEvent.getProductUnits();
        this.physicalState = productUpdatedEvent.getPhysicalState();
        this.packaging = productUpdatedEvent.getPackaging();
        this.safetyInstructions = productUpdatedEvent.getSafetyInstructions();
        this.hazardClassification = productUpdatedEvent.getHazardClassification();
        this.ppeRequired = productUpdatedEvent.getPpeRequired();
    }

    @EventSourcingHandler
    public void on(ProductTotalQuantityUpdatedEvent productTotalQuantityUpdatedEvent) {
        if (this.totalQuantity == null) {
            this.totalQuantity = 0.0;
        }
        totalQuantity += productTotalQuantityUpdatedEvent.getQuantity();
    }

    @EventSourcingHandler
    public void on(ProductTotalQuantityReducedEvent productTotalQuantityReducedEvent) {
        if (this.totalQuantity == null || this.totalQuantity < productTotalQuantityReducedEvent.getQuantity()) {
            throw new BusinessException(ErrorCode.PRODUCT_OUT_OF_STOCK);
        } else {
            totalQuantity -= productTotalQuantityReducedEvent.getQuantity();
        }
    }

}
