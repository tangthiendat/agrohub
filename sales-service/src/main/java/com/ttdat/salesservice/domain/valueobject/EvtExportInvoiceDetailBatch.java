package com.ttdat.salesservice.domain.valueobject;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EvtExportInvoiceDetailBatch {
    String exportInvoiceDetailBatchId;

    String batchId;

    Double quantity;

    List<EvtExportInvoiceDetailBatchLocation> detailBatchLocations;
}
