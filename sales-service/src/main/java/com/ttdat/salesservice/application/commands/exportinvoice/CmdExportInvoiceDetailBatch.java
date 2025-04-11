package com.ttdat.salesservice.application.commands.exportinvoice;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class CmdExportInvoiceDetailBatch {
    String exportInvoiceDetailBatchId;

    String batchId;

    Double quantity;

    List<CmdExportInvoiceDetailBatchLocation> detailBatchLocations;
}
