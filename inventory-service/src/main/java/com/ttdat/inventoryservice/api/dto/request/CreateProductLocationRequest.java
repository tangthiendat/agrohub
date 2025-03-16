package com.ttdat.inventoryservice.api.dto.request;

import com.ttdat.inventoryservice.domain.entities.LocationStatus;
import com.ttdat.inventoryservice.domain.entities.RackType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateProductLocationRequest {
    Long warehouseId;

    String rackName;

    RackType rackType;

    Integer rowNumber;

    Integer columnNumber;

    LocationStatus status;
}
