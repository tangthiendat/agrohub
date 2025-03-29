package com.ttdat.debtservice.api.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreatePaymentDetailRequest {
    @NotBlank(message = "Debt account ID is required")
    String debtAccountId;

    @NotNull(message = "Total paid amount is required")
    @DecimalMin(value = "0.01", message = "Total paid amount must be greater than 0")
    @Digits(integer = 15, fraction = 2, message = "Total paid amount must have at most 15 digits and 2 decimal places")
    BigDecimal paymentAmount;
}
