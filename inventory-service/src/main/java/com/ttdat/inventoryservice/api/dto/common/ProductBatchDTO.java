package com.ttdat.inventoryservice.api.dto.common;

import com.ttdat.core.api.dto.response.ProductInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
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
    @Size(max = 50, message = "Batch ID must not exceed 50 characters")
    String batchId;

    @NotNull(message = "Product is required")
    ProductInfo product;

    @NotNull(message = "Manufacturing date is required")
    @PastOrPresent(message = "Manufacturing date must be in the past or present")
    LocalDate manufacturingDate;

    @NotNull(message = "Expiration date is required")
    @Future(message = "Expiration date must be in the future")
    LocalDate expirationDate;

    @NotNull(message = "Received date is required")
    @PastOrPresent(message = "Received date must be in the past or present")
    LocalDate receivedDate;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than 0")
    Integer quantity;

    @Valid
    List<ProductBatchLocationDTO> batchLocations;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;
}
