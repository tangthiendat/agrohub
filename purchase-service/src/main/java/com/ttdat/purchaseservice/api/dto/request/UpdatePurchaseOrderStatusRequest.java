package com.ttdat.purchaseservice.api.dto.request;

import com.ttdat.purchaseservice.domain.entities.PurchaseOrderStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdatePurchaseOrderStatusRequest {
    PurchaseOrderStatus status;
}
