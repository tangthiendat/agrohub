package com.ttdat.purchaseservice.api.dto.common;

import com.ttdat.core.api.dto.response.ProductInfo;
import com.ttdat.core.api.dto.response.ProductUnitInfo;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImportInvoiceDetailDTO {
    String importInvoiceDetailId;

    ProductInfo product;

    ProductUnitInfo productUnit;

    Integer quantity;

    BigDecimal unitPrice;
}
