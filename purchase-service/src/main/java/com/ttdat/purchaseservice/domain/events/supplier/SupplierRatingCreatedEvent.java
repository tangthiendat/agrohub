package com.ttdat.purchaseservice.domain.events.supplier;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SupplierRatingCreatedEvent {
    String ratingId;

    Long warehouseId;

    String supplierId;

    Integer trustScore;

    String comment;
}
