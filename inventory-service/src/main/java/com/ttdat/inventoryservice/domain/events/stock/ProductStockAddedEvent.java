package com.ttdat.inventoryservice.domain.events.stock;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ProductStockAddedEvent {
    String productStockId;

    Long warehouseId;

    String productId;

    Double quantity;
}
