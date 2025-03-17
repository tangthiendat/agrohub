package com.ttdat.inventoryservice.api.dto.common;

import com.ttdat.inventoryservice.domain.entities.LocationStatus;
import com.ttdat.inventoryservice.domain.entities.RackType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductLocationDTO {
    String locationId;

    Long warehouseId;

    String rackName;

    RackType rackType;

    Integer rowNumber;

    Integer columnNumber;

    LocationStatus status;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;
}
