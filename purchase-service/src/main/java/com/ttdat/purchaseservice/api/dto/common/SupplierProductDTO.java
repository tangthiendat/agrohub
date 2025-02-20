package com.ttdat.purchaseservice.api.dto.common;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SupplierProductDTO {

    String supplierProductId;

    SupplierDTO supplier;

    String productId;
}
