package com.ttdat.productservice.api.dto.common;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @Digits(integer = 13, fraction = 2, message = "Price must have at most 13 digits and 2 decimal places")
    BigDecimal price;

    @NotNull(message = "Valid from date is required")
    LocalDate validFrom;

    @Future(message = "Valid to date must be in the future")
    LocalDate validTo;
}
