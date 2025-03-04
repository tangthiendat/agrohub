package com.ttdat.purchaseservice.api.dto.common;

import com.ttdat.purchaseservice.api.dto.response.PodProductDTO;
import com.ttdat.purchaseservice.api.dto.response.PodProductUnitDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseOrderDetailDTO {
    String purchaseOrderDetailId;

    PodProductDTO product;

    PodProductUnitDTO productUnit;

    Integer quantity;

    String unitPrice;
}
