package com.ttdat.salesservice.api.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateDetailBatch {
    @NotBlank(message = "Batch ID is required")
    @Size(max = 50, message = "Batch ID must not exceed 50 characters")
    String batchId;

    @NotNull(message = "Quantity is required")
    @DecimalMin(value = "0.01", message = "Quantity must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Quantity must have at most 10 digits and 2 decimal places")
    Double quantity;

    @NotNull(message = "Batch locations are required")
    @Size(min = 1, message = "At least one batch location is required")
    @Valid
    List<CreateDetailBatchLocation> batchLocations;
}
