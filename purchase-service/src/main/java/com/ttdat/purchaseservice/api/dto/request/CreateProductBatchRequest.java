package com.ttdat.purchaseservice.api.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateProductBatchRequest {
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
    @Min(value = 1, message = "Quantity must be greater than 0")
    Integer quantity;
}
