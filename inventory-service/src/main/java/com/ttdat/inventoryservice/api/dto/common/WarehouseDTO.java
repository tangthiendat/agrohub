package com.ttdat.inventoryservice.api.dto.common;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WarehouseDTO {
    Long warehouseId;

    @NotBlank(message = "Warehouse name is required")
    String warehouseName;

    String address;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;
}
