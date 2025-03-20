package com.ttdat.core.api.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductInfo {
    String productId;

    String productName;

    CategoryInfo category;

    List<ProductUnitInfo> productUnits;

    String imageUrl;
}
