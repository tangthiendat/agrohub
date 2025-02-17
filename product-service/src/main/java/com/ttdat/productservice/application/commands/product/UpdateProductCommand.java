package com.ttdat.productservice.application.commands.product;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.ttdat.productservice.domain.entities.PhysicalState;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class UpdateProductCommand {
    @TargetAggregateIdentifier
    String productId;

    String productName;

    String description;

    Integer totalQuantity;

    String imageUrl;

    Long categoryId;

    Integer defaultExpDays;

    String storageInstructions;

    List<CmdProductUnit> productUnits;

    PhysicalState physicalState;

    String packaging;

    String safetyInstructions;

    String hazardClassification;

    String ppeRequired;
}
