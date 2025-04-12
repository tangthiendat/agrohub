package com.ttdat.salesservice.api.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateDetailBatchLocation {

    @NotBlank(message = "Batch location ID is required")
    @Size(max = 50, message = "Batch location ID must not exceed 50 characters")
    String batchLocationId;

    @NotNull(message = "Quantity is required")
    @DecimalMin(value = "0.01", message = "Quantity must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Quantity must have at most 10 digits and 2 decimal places")
    Double quantity;
}
