package com.ttdat.purchaseservice.api.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdatePurchaseOrderDetailRequest {
    @NotBlank(message = "Product ID is required")
    @Size(max = 50, message = "Product ID must not exceed 50 characters")
    String productId;

    @NotBlank(message = "Product unit ID is required")
    @Size(max = 50, message = "Product unit ID must not exceed 50 characters")
    String productUnitId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be greater than 0")
    Integer quantity;

    @NotNull(message = "Unit price is required")
    @DecimalMin(value = "0", message = "Unit price must be greater than or equal to 0")
    @Digits(integer = 13, fraction = 2, message = "Unit price must have at most 13 digits and 2 decimal places")
    BigDecimal unitPrice;
}
