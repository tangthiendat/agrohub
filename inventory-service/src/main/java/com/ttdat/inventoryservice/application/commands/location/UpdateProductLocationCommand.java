package com.ttdat.inventoryservice.application.commands.location;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.ttdat.inventoryservice.domain.entities.LocationStatus;
import com.ttdat.inventoryservice.domain.entities.RackType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class UpdateProductLocationCommand {
    String locationId;

    Long warehouseId;

    String rackName;

    RackType rackType;

    Integer rowNumber;

    Integer columnNumber;

    LocationStatus status;
}
