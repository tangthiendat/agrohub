package com.ttdat.purchaseservice.application.commands.supplier;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class UpdateSupplierRatingCommand {
    String ratingId;

    Long warehouseId;

    String supplierId;

    Integer trustScore;

    String comment;
}
