package com.ttdat.purchaseservice.api.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateSupplierRatingRequest {
    String ratingId;

    @NotNull(message = "Warehouse ID is required")
    Long warehouseId;

    @Min(value = 0, message = "Score must be greater than or equal to 0")
    @Max(value = 100, message = "Score must be less than or equal to 100")
    Integer trustScore;

    String comment;
}
