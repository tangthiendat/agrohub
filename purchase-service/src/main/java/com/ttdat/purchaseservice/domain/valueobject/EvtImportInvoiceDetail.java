package com.ttdat.purchaseservice.domain.valueobject;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EvtImportInvoiceDetail {
    String importInvoiceDetailId;

    String productId;

    String productUnitId;

    Integer quantity;

    BigDecimal unitPrice;
}
