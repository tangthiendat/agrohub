package com.ttdat.productservice.domain.valueobject;

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
public class EvtProductUnit {
    String productUnitId;

    Long unitId;

    Double conversionFactor;

    boolean isDefault;

    List<EvtProductUnitPrice> productUnitPrices;
}
