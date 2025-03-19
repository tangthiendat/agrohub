package com.ttdat.inventoryservice.api.dto.common;

import com.ttdat.core.api.dto.response.ProductInfo;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductStockDTO {
    String productStockId;

    ProductInfo product;

    Double quantity;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;
}
