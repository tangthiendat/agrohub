package com.ttdat.productservice.domain.valueobject;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EvtProductUnitPrice {
    String productUnitPriceId;

    BigDecimal price;

    LocalDate validFrom;

    LocalDate validTo;
}

