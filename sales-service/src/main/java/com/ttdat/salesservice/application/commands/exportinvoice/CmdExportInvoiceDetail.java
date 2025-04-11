package com.ttdat.salesservice.application.commands.exportinvoice;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class CmdExportInvoiceDetail {
    String exportInvoiceDetailId;

    String productId;

    String productUnitId;

    Integer quantity;

    BigDecimal unitPrice;

    List<CmdExportInvoiceDetailBatch> detailBatches;
}
