package com.ttdat.salesservice.domain.valueobject;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EvtExportInvoiceDetail {
    String exportInvoiceDetailId;

    String productId;

    String productUnitId;

    Integer quantity;

    BigDecimal unitPrice;

    List<EvtExportInvoiceDetailBatch> detailBatches;
}
