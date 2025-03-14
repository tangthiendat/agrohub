package com.ttdat.purchaseservice.api.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdatePurchaseOrderDetailRequest {
    String productId;

    String productUnitId;

    Integer quantity;

    BigDecimal unitPrice;
}
