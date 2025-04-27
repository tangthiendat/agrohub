package com.ttdat.salesservice.application.commands.exportinvoice;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class CmdExportInvoiceDetailBatchLocation {
    String batchLocationId;

    Double quantity;
}
