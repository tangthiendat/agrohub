package com.ttdat.purchaseservice.domain.events.purchaseorder;

import com.ttdat.purchaseservice.domain.entities.PurchaseOrderStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseOrderStatusUpdatedEvent {
    String purchaseOrderId;

    PurchaseOrderStatus status;
}
