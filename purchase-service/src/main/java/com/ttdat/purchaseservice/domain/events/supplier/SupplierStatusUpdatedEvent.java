package com.ttdat.purchaseservice.domain.events.supplier;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SupplierStatusUpdatedEvent {
    String supplierId;
    boolean active;
}
