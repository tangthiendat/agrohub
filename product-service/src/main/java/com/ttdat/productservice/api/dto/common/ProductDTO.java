package com.ttdat.productservice.api.dto.common;

import com.ttdat.productservice.domain.entities.PhysicalState;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
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
    @Size(max = 150, message = "Product name must not exceed 150 characters")
    String productName;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    String description;

    @PositiveOrZero(message = "Total quantity must not be negative")
    Double totalQuantity;

    Double currentStock;

    @Size(max = 255, message = "Image URL must not exceed 255 characters")
    String imageUrl;

    @NotNull(message = "Category is required")
    CategoryDTO category;

    @Positive(message = "Default expiry days must be a positive number")
    @NotNull(message = "Default expiry days is required")
    @Max(value = 3650, message = "Default expiry days must not exceed 10 years")
    Integer defaultExpDays;

    @Size(max = 500, message = "Storage instructions must not exceed 500 characters")
    String storageInstructions;

    @Valid
    @NotEmpty(message = "Product must have at least one unit")
    List<ProductUnitDTO> productUnits;

    @NotNull(message = "Physical state is required")
    PhysicalState physicalState;

    @Size(max = 100, message = "Packaging must not exceed 100 characters")
    String packaging;

    @Size(max = 500, message = "Safety instructions must not exceed 500 characters")
    String safetyInstructions;

    @Size(max = 100, message = "Hazard classification must not exceed 100 characters")
    String hazardClassification;

    @Size(max = 200, message = "PPE required must not exceed 200 characters")
    String ppeRequired;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;
}
