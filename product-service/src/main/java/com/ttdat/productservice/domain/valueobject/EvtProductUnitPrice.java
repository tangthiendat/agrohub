package com.ttdat.productservice.domain.valueobject;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EvtProductUnitPrice {
    String productUnitPriceId;

    BigDecimal price;

    LocalDate validFrom;

    LocalDate validTo;
}

