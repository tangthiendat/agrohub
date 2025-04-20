package com.ttdat.productservice.domain.events.product;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductTotalQuantityAddedEvent {
    String productId;

    Double quantity;
}
