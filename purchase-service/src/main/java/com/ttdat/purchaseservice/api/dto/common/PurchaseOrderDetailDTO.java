package com.ttdat.purchaseservice.api.dto.common;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseOrderDetailDTO {
    String purchaseOrderDetailId;

    String productId;

    String productUnitId;

    Integer quantity;

    String unitPrice;
}
