package com.ttdat.purchaseservice.api.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateImportInvoiceDetailRequest {
    String productId;

    String productUnitId;

    Integer quantity;

    BigDecimal unitPrice;

    List<CreateProductBatchRequest> batches;
}
