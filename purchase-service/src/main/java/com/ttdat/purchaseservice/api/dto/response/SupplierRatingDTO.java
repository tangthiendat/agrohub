package com.ttdat.purchaseservice.api.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SupplierRatingDTO {
    String ratingId;

    Integer trustScore;

    String comment;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;
}
