package com.ttdat.productservice.api.dto.common;

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

    UnitDTO unit;

    Double conversionFactor;

    boolean isDefault;

    List<ProductUnitPriceDTO> productUnitPrices;
}
