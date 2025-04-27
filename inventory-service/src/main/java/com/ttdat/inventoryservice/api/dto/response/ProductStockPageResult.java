package com.ttdat.inventoryservice.api.dto.response;

import com.ttdat.core.api.dto.response.PaginationMeta;
import com.ttdat.inventoryservice.api.dto.common.ProductStockDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductStockPageResult {
    PaginationMeta meta;
    List<ProductStockDTO> content;
}
