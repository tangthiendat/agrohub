package com.ttdat.purchaseservice.api.dto.common;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SupplierProductDTO {

    String supplierProductId;

    @NotNull(message = "Supplier is required")
    SupplierDTO supplier;

    @NotNull(message = "Product ID is required")
    @Size(max = 50, message = "Product ID must not exceed 50 characters")
    String productId;
}
