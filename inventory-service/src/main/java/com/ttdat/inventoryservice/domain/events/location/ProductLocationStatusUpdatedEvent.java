package com.ttdat.inventoryservice.domain.events.location;

import com.ttdat.inventoryservice.domain.entities.LocationStatus;
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
public class ProductLocationStatusUpdatedEvent {
    String locationId;

    LocationStatus status;
}
