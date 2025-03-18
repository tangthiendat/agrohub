package com.ttdat.inventoryservice.api.dto.common;

import com.ttdat.core.api.dto.response.ProductInfo;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductBatchDTO {
    String batchId;

    ProductInfo product;

    LocalDate manufacturingDate;

    LocalDate expirationDate;

    LocalDate receivedDate;

    Integer quantity;

    List<ProductBatchLocationDTO> batchLocations;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;
}
