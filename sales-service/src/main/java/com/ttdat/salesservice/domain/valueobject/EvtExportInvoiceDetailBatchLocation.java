package com.ttdat.salesservice.domain.valueobject;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EvtExportInvoiceDetailBatchLocation {
    String batchLocationId;

    Double quantity;
}
