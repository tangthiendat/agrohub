package com.ttdat.purchaseservice.domain.events.importinvoice;

import com.ttdat.purchaseservice.domain.entities.DiscountType;
import com.ttdat.purchaseservice.domain.valueobject.EvtImportInvoiceDetail;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImportInvoiceCreatedEvent {
    String importInvoiceId;

    Long warehouseId;

    String supplierId;

    String userId;

    LocalDate createdDate;

    String note;

    List<EvtImportInvoiceDetail> importInvoiceDetails;

    BigDecimal totalAmount;

    BigDecimal discountValue;

    DiscountType discountType;

    BigDecimal vatRate;

    BigDecimal finalAmount;
}
