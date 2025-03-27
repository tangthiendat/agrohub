package com.ttdat.inventoryservice.api.dto.common;

import com.ttdat.inventoryservice.domain.entities.LocationStatus;
import com.ttdat.inventoryservice.domain.entities.RackType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductLocationDTO {
    @Size(max = 50, message = "Location ID must not exceed 50 characters")
    String locationId;

    @NotNull(message = "Warehouse ID is required")
    @Positive(message = "Warehouse ID must be positive")
    Long warehouseId;

    @NotBlank(message = "Rack name is required")
    @Size(max = 10, message = "Rack name must not exceed 10 characters")
    String rackName;

    @NotNull(message = "Rack type is required")
    RackType rackType;

    @NotNull(message = "Row number is required")
    @Positive(message = "Row number must be positive")
    Integer rowNumber;

    @NotNull(message = "Column number is required")
    @Positive(message = "Column number must be positive")
    Integer columnNumber;

    @NotNull(message = "Location status is required")
    LocationStatus status;

    @Size(max = 255, message = "Reason must not exceed 255 characters")
    String reason;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;
}
