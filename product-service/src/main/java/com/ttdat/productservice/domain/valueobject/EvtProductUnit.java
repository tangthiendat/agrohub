package com.ttdat.productservice.domain.valueobject;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EvtProductUnit {
    String productUnitId;

    Long unitId;

    Double conversionFactor;

    boolean isDefault;

    List<EvtProductUnitPrice> productUnitPrices;
}
