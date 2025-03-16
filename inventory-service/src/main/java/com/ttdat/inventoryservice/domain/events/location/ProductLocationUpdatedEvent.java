package com.ttdat.inventoryservice.domain.events.location;

import com.ttdat.inventoryservice.domain.entities.LocationStatus;
import com.ttdat.inventoryservice.domain.entities.RackType;
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
public class ProductLocationUpdatedEvent {
    String locationId;

    Long warehouseId;

    String rackName;

    RackType rackType;

    Integer rowNumber;

    Integer columnNumber;

    LocationStatus status;
}
