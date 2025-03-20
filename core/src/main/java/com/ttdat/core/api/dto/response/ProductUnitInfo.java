package com.ttdat.core.api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductUnitInfo {
    String productUnitId;

    UnitInfo unit;

    Double conversionFactor;

    @JsonProperty("is_default")
    boolean isDefault;
}
