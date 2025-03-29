package com.ttdat.productservice.api.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductUnitDTO {
    String productUnitId;

    @NotNull(message = "Unit is required")
    UnitDTO unit;

    @NotNull(message = "Conversion factor is required")
    @Positive(message = "Conversion factor must be positive")
    Double conversionFactor;

    @JsonProperty("is_default")
    boolean isDefault;

    @Valid
    List<ProductUnitPriceDTO> productUnitPrices;
}
