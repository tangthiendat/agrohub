package com.ttdat.productservice.application.commands.product;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class CmdProductUnitPrice {
    String productUnitPriceId;

    BigDecimal price;

    LocalDate validFrom;

    LocalDate validTo;
}
