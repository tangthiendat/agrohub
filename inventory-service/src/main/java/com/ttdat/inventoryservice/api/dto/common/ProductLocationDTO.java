package com.ttdat.inventoryservice.api.dto.common;

import com.ttdat.core.api.dto.response.WarehouseInfo;
import com.ttdat.inventoryservice.domain.entities.LocationStatus;
import com.ttdat.inventoryservice.domain.entities.RackType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductLocationDTO {
    String locationId;

    WarehouseInfo warehouse;

    String rackName;

    RackType rackType;

    Integer rowNumber;

    Integer columnNumber;

    LocationStatus status;
}
