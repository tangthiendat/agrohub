package com.ttdat.inventoryservice.api.dto.common;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductBatchLocationDTO {
    String batchLocationId;

    ProductLocationDTO productLocation;

    Double quantity;
}
