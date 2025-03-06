package com.ttdat.purchaseservice.domain.aggregate;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EvtPurchaseOrderDetail {
    String purchaseOrderDetailId;

    String productId;

    String productUnitId;

    Integer quantity;

    BigDecimal unitPrice;
}
