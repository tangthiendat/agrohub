package com.ttdat.productservice.api.dto.common;

import com.ttdat.productservice.domain.entities.PhysicalState;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDTO {
    String productId;

    @NotBlank(message = "Product name is required")
    String productName;

    String description;

    String imageUrl;

    @NotNull(message = "Category is required")
    CategoryDTO category;

    @Positive(message = "Default expiry days must be a positive number")
    Integer defaultExpDays;

    String storageInstructions;

    List<ProductUnitDTO> productUnits;

    PhysicalState physicalState;

    String packaging;

    String safetyInstructions;

    String hazardClassification;

    String ppeRequired;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;
}
