package com.ttdat.productservice.domain.events.product;

import com.ttdat.productservice.domain.entities.PhysicalState;
import com.ttdat.productservice.domain.valueobject.EvtProductUnit;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCreatedEvent {
    String productId;

    String productName;

    String description;

    Integer totalQuantity;

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
}
