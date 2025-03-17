package com.ttdat.core.api.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductInfo {
    String productId;

    String productName;

    CategoryInfo category;

    String imgUrl;
}
