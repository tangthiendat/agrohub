package com.ttdat.purchaseservice.domain.events.purchaseorder;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseOrderCancelledEvent {
    String purchaseOrderId;

    String cancelReason;
}
