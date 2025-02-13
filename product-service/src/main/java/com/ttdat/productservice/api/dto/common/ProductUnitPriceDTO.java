package com.ttdat.productservice.api.dto.common;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductUnitPriceDTO {
    String productUnitPriceId;

    BigDecimal price;

    LocalDate validFrom;

    LocalDate validTo;
}
