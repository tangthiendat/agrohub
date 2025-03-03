package com.ttdat.purchaseservice.api.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreatePurchaseOrderDetailRequest {
    String productId;

    String productUnitId;

    Integer quantity;
}
