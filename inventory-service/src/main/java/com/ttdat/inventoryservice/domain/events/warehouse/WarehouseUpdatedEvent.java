package com.ttdat.inventoryservice.domain.events.warehouse;

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
public class WarehouseUpdatedEvent {
    Long warehouseId;

    String warehouseName;

    String address;
}
