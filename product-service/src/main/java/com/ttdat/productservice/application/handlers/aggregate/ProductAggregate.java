package com.ttdat.productservice.application.handlers.aggregate;

import com.ttdat.productservice.application.commands.product.CreateProductCommand;
import com.ttdat.productservice.domain.entities.PhysicalState;
import com.ttdat.productservice.domain.events.product.ProductCreatedEvent;
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
import java.util.stream.Collectors;

@Slf4j
@Aggregate(type = "ProductAggregate", snapshotTriggerDefinition = "productSnapshotTriggerDefinition")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ProductAggregate {
    @AggregateIdentifier
    String productId;

    String productName;

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
                                            .collect(Collectors.toList())
                                    : List.of();
                            return EvtProductUnit.builder()
                                    .productUnitId(productUnit.getProductUnitId())
                                    .unitId(productUnit.getUnitId())
                                    .productUnitPrices(evtProductUnitPrices)
                                    .conversionFactor(productUnit.getConversionFactor())
                                    .isDefault(productUnit.isDefault())
                                    .build();
                        }).collect(Collectors.toList())
                : List.of();
        ProductCreatedEvent productCreatedEvent = ProductCreatedEvent.builder()
                .productId(createProductCommand.getProductId())
                .productName(createProductCommand.getProductName())
                .description(createProductCommand.getDescription())
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

    @EventSourcingHandler
    public void on(ProductCreatedEvent productCreatedEvent) {
        this.productId = productCreatedEvent.getProductId();
        this.productName = productCreatedEvent.getProductName();
        this.description = productCreatedEvent.getDescription();
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


}
