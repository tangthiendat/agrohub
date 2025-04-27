package com.ttdat.productservice.application.handlers.command;

import com.ttdat.core.application.commands.product.AddProductTotalQuantityCommand;
import com.ttdat.core.application.commands.product.ReduceProductTotalQuantityCommand;
import com.ttdat.productservice.application.commands.product.CreateProductCommand;
import com.ttdat.productservice.application.commands.product.UpdateProductCommand;
import com.ttdat.productservice.domain.events.product.ProductCreatedEvent;
import com.ttdat.productservice.domain.events.product.ProductTotalQuantityAddedEvent;
import com.ttdat.productservice.domain.events.product.ProductTotalQuantityReducedEvent;
import com.ttdat.productservice.domain.events.product.ProductUpdatedEvent;
import com.ttdat.productservice.domain.valueobject.EvtProductUnit;
import com.ttdat.productservice.domain.valueobject.EvtProductUnitPrice;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.GenericEventMessage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductCommandHandler {
    private final EventBus eventBus;

    @CommandHandler
    public void handle(CreateProductCommand createProductCommand){
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
        eventBus.publish(GenericEventMessage.asEventMessage(productCreatedEvent));
    }

    @CommandHandler
    public void handle(UpdateProductCommand updateProductCommand){
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
        eventBus.publish(GenericEventMessage.asEventMessage(productUpdatedEvent));
    }

    @CommandHandler
    public void handle(AddProductTotalQuantityCommand addProductTotalQuantityCommand) {
        ProductTotalQuantityAddedEvent productTotalQuantityAddedEvent = ProductTotalQuantityAddedEvent.builder()
                .productId(addProductTotalQuantityCommand.getProductId())
                .quantity(addProductTotalQuantityCommand.getQuantity())
                .build();
        eventBus.publish(GenericEventMessage.asEventMessage(productTotalQuantityAddedEvent));
    }

    @CommandHandler
    public void handle(ReduceProductTotalQuantityCommand reduceProductTotalQuantityCommand) {
        ProductTotalQuantityReducedEvent productTotalQuantityReducedEvent = ProductTotalQuantityReducedEvent.builder()
                .productId(reduceProductTotalQuantityCommand.getProductId())
                .quantity(reduceProductTotalQuantityCommand.getQuantity())
                .build();
        eventBus.publish(GenericEventMessage.asEventMessage(productTotalQuantityReducedEvent));
    }
}
