package com.ttdat.debtservice.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateReceiptDetailRequest {
    @NotBlank(message = "Debt account ID is required")
    String debtAccountId;

    BigDecimal receiptAmount;
}