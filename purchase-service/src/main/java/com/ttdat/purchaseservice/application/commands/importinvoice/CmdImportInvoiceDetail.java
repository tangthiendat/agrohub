package com.ttdat.purchaseservice.application.commands.importinvoice;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class CmdImportInvoiceDetail {
    String importInvoiceDetailId;

    String productId;

    String productUnitId;

    Integer quantity;

    BigDecimal unitPrice;
}
