package com.ttdat.salesservice.api.dto.common;

import com.ttdat.core.api.dto.response.ProductBatchInfo;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExportInvoiceDetailBatchDTO {
    String exportInvoiceDetailBatchId;

    ProductBatchInfo productBatch;

    Double quantity;
}
